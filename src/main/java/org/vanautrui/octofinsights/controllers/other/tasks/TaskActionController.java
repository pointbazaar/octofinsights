package org.vanautrui.octofinsights.controllers.other.tasks;

import org.vanautrui.octofinsights.services.TasksService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVPOSTHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import spark.Request;
import spark.Response;

import static java.lang.Integer.parseInt;

public final class TaskActionController {

  public static Object post(Request request, Response response) {
    if(        vhttpPostRequest.session().isPresent()
            && vhttpPostRequest.session().get().containsKey("authenticated")
            && vhttpPostRequest.session().get().get("authenticated").equals("true")
            && vhttpPostRequest.session().get().containsKey("user_id")
    ) {
      final int user_id = parseInt(vhttpPostRequest.session().get().get("user_id"));

      final String action = vhttpPostRequest.getQueryParam("action");
      final int id = parseInt(vhttpPostRequest.getQueryParam("id"));

      final String redirect_url = vhttpPostRequest.getQueryParam("redirect");

      switch (action){
        case "complete": TasksService.completeTask(id,user_id); break;
        case "spend1hour": TasksService.spend1hour(id,user_id); break;
        case "delete": TasksService.deleteTask(id,user_id); break;
        default: throw new Exception("unrecognized query parameter value");
      }

      response.redirect(redirect_url);
    }
    response.redirect("/login");
    return "";
  }
}
