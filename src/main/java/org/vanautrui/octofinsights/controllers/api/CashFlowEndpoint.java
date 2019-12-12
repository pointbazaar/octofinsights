package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VJsonResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.jooq.impl.DSL.*;
import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.Tables.SALES;


public final class CashFlowEndpoint {

    public static Object get(Request req, Response response {

        req.cookie()

        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            Connection conn= null;
            try {
                conn = DBUtils.makeDBConnection();
            } catch (Exception e) {
                response.status(500);
                response.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
                e.printStackTrace();
            }
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node =  mapper.createArrayNode();

        /*
        Result<Record2<Integer, String>> result = create.select(
                DSL.coalesce(SALES.PRICE_OF_SALE,EXPENSES.EXPENSE_VALUE).as("value"),
                DSL.coalesce(SALES.PRODUCT_OR_SERVICE,EXPENSES.EXPENSE_NAME).as("label")
        ).from(SALES,EXPENSES).fetch();
         */

            int current_year = LocalDateTime.now().getYear();


            //Result<Record3<Integer, String, Timestamp>> result =
            SelectLimitStep<Record2<BigDecimal, Integer>> record3s = create.select(
                    sum((SALES.PRICE_OF_SALE)).as("value"),
                    month(SALES.TIME_OF_SALE).as("month")
            )
                    .from(SALES).where(SALES.USER_ID.eq(user_id).and(year(SALES.TIME_OF_SALE).eq(current_year))).groupBy(year(SALES.TIME_OF_SALE),month(SALES.TIME_OF_SALE))

                    .union(
                            create.select(
                                    sum((EXPENSES.EXPENSE_VALUE)).as("value"),
                                    month(EXPENSES.EXPENSE_DATE).as("month")
                            )
                                    .from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id).and(year(EXPENSES.EXPENSE_DATE).eq(current_year))).groupBy(year(EXPENSES.EXPENSE_DATE),month(EXPENSES.EXPENSE_DATE))
                    )
                    .orderBy(2);
            //.fetch();

            Result<Record2<BigDecimal, Integer>> result =record3s.fetch();

            //List<Integer> sales_list = new ArrayList<>();

            for(Record2<BigDecimal, Integer> r : result){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value",r.value1());

                String month_str = LocalDateTime.of(current_year,r.value2(),1,1,1).getMonth().toString();

                objectNode.put("label",month_str+" "+current_year);
                //objectNode.put("label",r.value2().substring(0,Math.min(r.value2().length(),25)));

                node.add(objectNode);
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.status(200);
            response.type(ContentType.APPLICATION_JSON.toString());
            return node.toPrettyString();
        }else{

            response.status(400);
            response.type(ContentType.TEXT_PLAIN.toString());
            return "Bad Request, no user_id found in session.";
        }
    }
}
