package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.services.TransactionsService;
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

    public static Object get(Request req, Response res) {


        if(req.session().attributes().contains("user_id")){
            int user_id = Integer.parseInt(req.session().attribute("user_id"));

            Connection conn= null;
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

        /*
        Result<Record2<Integer, String>> result = create.select(
                DSL.coalesce(SALES.PRICE_OF_SALE,EXPENSES.EXPENSE_VALUE).as("value"),
                DSL.coalesce(SALES.PRODUCT_OR_SERVICE,EXPENSES.EXPENSE_NAME).as("label")
        ).from(SALES,EXPENSES).fetch();
         */

            final int current_year = LocalDateTime.now().getYear();

	        Result<Record2<BigDecimal, Integer>> result = null;
	        try {
		        result = TransactionsService.getAllTransactionsForUserIdInThisYearGroupedByMonth(user_id);
	        } catch (Exception e) {
		        e.printStackTrace();
	        }


	        for(Record2<BigDecimal, Integer> r : result){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value",r.value1());

                String month_str = LocalDateTime.of(current_year,r.value2(),1,1,1).getMonth().toString();

                objectNode.put("label",month_str+" "+current_year);

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
