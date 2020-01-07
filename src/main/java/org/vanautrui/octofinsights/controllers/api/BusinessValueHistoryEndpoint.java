package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.Record3;
import org.jooq.Result;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.services.TransactionsService;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;


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

            final ObjectMapper mapper = new ObjectMapper();
            final ArrayNode node =  mapper.createArrayNode();

            //int current_year = LocalDateTime.now().getYear();
            Result<Record3<BigDecimal, Integer, Integer>> fetch=null;
            try {
                fetch = TransactionsService.getAllTransactionsForUserIdGroupedByMonthAndYear(user_id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for(Record3<BigDecimal, Integer, Integer> r : fetch){
                final ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value",r.value1());

                LocalDateTime ldt = LocalDateTime.of(r.value3(), r.value2(), 1, 1, 1);

                final String month_str = ldt.getMonth().toString();

                objectNode.put("label",month_str+" "+ldt.getYear());

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
