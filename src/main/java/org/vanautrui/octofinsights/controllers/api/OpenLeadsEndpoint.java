package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.LeadsService;
import spark.Request;
import spark.Response;


public final class OpenLeadsEndpoint {

    public static Object get(Request req, Response res) {
        if(req.session().attributes().contains("user_id")){
            final int user_id = Integer.parseInt(req.session().attribute("user_id"));

            final long count;
            try {
                count = LeadsService.getOpenLeadsCount(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            final ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("value",count);

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
