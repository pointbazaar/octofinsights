package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.ProjectsService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VJsonResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;
import spark.Request;
import spark.Response;


public final class ActiveProjectsEndpoint {


    public static Object get(Request req, Response res){
        if(
                req.session().isPresent()
                        && req.session().get().containsKey("user_id")
        ){
            final int user_id = Integer.parseInt(req.session().get().get("user_id"));

            final long count;
            try {
                count = ProjectsService.getActiveProjectsCount(user_id);
            } catch (Exception e) {
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
                e.printStackTrace();
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
