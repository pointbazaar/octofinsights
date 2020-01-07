package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.Record3;
import org.jooq.Record2;
import org.jooq.Result;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.services.TransactionsService;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.vanautrui.octofinsights.services.MostProfitableCustomersService;

public class MostProfitableCustomersEndpoint {

    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("user_id")){
            final int user_id = Integer.parseInt(req.session().attribute("user_id"));

            final ObjectMapper mapper = new ObjectMapper();
            final ArrayNode node =  mapper.createArrayNode();

            Result<Record2<BigDecimal, String>> fetch=null;
            try {
                fetch = MostProfitableCustomersService.getByUserId(user_id);//TODO: get the most profitable customers and the sum of the sales to them
            } catch (Exception e) {
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                e.printStackTrace();
                return e.toString();
            }

            for(Record2<BigDecimal, String> r : fetch){
                final ObjectNode obj = mapper.createObjectNode();
                obj.put("value",r.value1());
                obj.put("label",r.value2());
                node.add(obj);
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
