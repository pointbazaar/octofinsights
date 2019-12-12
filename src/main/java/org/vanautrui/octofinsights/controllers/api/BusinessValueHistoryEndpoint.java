package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.jooq.impl.DSL.*;
import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.Tables.SALES;


public class BusinessValueHistoryEndpoint {


    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("user_id")){
            int user_id = Integer.parseInt(req.session().attribute("user_id"));

            final Connection conn;
            try {
                conn = DBUtils.makeDBConnection();
            } catch (Exception e) {
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                e.printStackTrace();
                return e.getMessage();
            }
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node =  mapper.createArrayNode();

            //int current_year = LocalDateTime.now().getYear();



            SelectLimitStep<Record3<BigDecimal,Integer, Integer>> record3s = create.select(
                    sum((SALES.PRICE_OF_SALE)).as("value"),
                    month(SALES.TIME_OF_SALE).as("month"),
                    year(SALES.TIME_OF_SALE).as("year")
            )
                    .from(SALES).where(SALES.USER_ID.eq(user_id)).groupBy(year(SALES.TIME_OF_SALE),month(SALES.TIME_OF_SALE))

                    .union(
                            create.select(
                                    sum((EXPENSES.EXPENSE_VALUE)).as("value"),
                                    month(EXPENSES.EXPENSE_DATE).as("month"),
                                    year(EXPENSES.EXPENSE_DATE).as("year")
                            )
                                    .from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id)).groupBy(year(EXPENSES.EXPENSE_DATE),month(EXPENSES.EXPENSE_DATE))
                    )
                    .orderBy(3,2);


            Result<Record3<BigDecimal,Integer, Integer>> result =record3s.fetch();


            for(Record3<BigDecimal,Integer, Integer> r : result){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value",r.value1());

                String month_str = LocalDateTime.of(2000,r.value2(),1,1,1).getMonth().toString();
                int year = r.value3();

                objectNode.put("label",month_str+" "+year);

                node.add(objectNode);
            }


            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            res.status(200);
            res.type(ContentType.APPLICATION_JSON.toString());
            return node.toPrettyString();
        }else{

            res.status(400);
            res.type(ContentType.TEXT_PLAIN.toString());
            return "Bad Request, no user_id found in session.";
        }
    }
}
