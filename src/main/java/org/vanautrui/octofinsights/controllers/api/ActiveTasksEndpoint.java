package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.TasksService;
import spark.Request;
import spark.Response;


public final class ActiveTasksEndpoint  {

    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("user_id")){
            int user_id = Integer.parseInt(req.session().attribute("user_id"));

            long count = 0;
            try {
                count = TasksService.countTasksByUserId(user_id);
            } catch (Exception e) {
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                e.printStackTrace();
                return e.getMessage();
            }

            ObjectNode node = (new ObjectMapper()).createObjectNode();

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
