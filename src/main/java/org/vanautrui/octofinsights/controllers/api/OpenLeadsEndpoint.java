package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.LeadsService;
import org.vanautrui.octofinsights.services.TasksService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VJsonResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;
import spark.Request;
import spark.Response;


public final class OpenLeadsEndpoint {

    public static Object get(Request request, Response response) {
        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            final int user_id = Integer.parseInt(req.session().get().get("user_id"));

            final long count;
            try {
                count = LeadsService.getOpenLeadsCount(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                response.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            final ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("value",count);

            response.status(200);
            response.type(ContentType.APPLICATION_JSON.toString());
            return (node.toPrettyString());
        }else{

            response.status(400);
            response.type(ContentType.TEXT_PLAIN.toString());
            return ("Bad Request, no user_id found in session.");
        }
    }
}
