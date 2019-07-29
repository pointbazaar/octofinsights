package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaJSONResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;

import static org.jooq.impl.DSL.*;
import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.Tables.SALES;


public class CashFlowEndpoint extends VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest req, VaquitaApp app) throws Exception {

        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            Connection conn= DBUtils.makeDBConnection();
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

            conn.close();

            return new VaquitaJSONResponse(200,node);
        }else{
            return new VaquitaTextResponse(400, "Bad Request, no user_id found in session.");
        }




    }

    @Override
    public VaquitaJSONResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return null;
    }
}
