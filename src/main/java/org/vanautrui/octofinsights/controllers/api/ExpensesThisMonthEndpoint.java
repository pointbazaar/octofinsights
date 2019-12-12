package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.ExpensesService;
import spark.Request;
import spark.Response;


public final class ExpensesThisMonthEndpoint {

    public static Object get(Request req, Response res) {
        if(req.session().attributes().contains("user_id")){
            int user_id = Integer.parseInt(req.session().attribute("user_id"));


            long balance = 0;
            try {
                balance = ExpensesService.getTotalForThisMonth(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("value",balance);

            res.status(200);
            res.type(ContentType.APPLICATION_JSON.toString());
            return (node.toPrettyString());
        }else{

            res.status(400);
            res.type(ContentType.TEXT_PLAIN.toString());
            return ("Bad Request, no user_id found in session.");
        }
    }
}
