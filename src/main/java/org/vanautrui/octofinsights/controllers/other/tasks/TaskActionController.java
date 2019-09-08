package org.vanautrui.octofinsights.controllers.other.tasks;

import org.vanautrui.octofinsights.services.TasksService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import static java.lang.Integer.parseInt;

public class TaskActionController extends VaquitaController {
  @Override
  public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest vaquitaHTTPJustRequest, VaquitaApp vaquitaApp) throws Exception {
    return null;
  }

  @Override
  public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest, VaquitaApp vaquitaApp) throws Exception {

    IVaquitaHTTPRequest request = vaquitaHTTPEntityEnclosingRequest;
    if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
            && request.session().get().containsKey("user_id")
    ) {
      int user_id = parseInt(request.session().get().get("user_id"));

      String action = vaquitaHTTPEntityEnclosingRequest.getQueryParameter("action");
      int id = parseInt(vaquitaHTTPEntityEnclosingRequest.getQueryParameter("id"));

      String redirect_url = request.getQueryParameter("redirect");

      switch (action){
        case "complete":
          TasksService.completeTask(id,user_id);
          break;
        case "spend1hour":
          TasksService.spend1hour(id,user_id);
          break;
        case "delete":
          TasksService.deleteTask(id,user_id);
          break;
        default:
          throw new Exception("unrecognized query parameter value");
      }

      return new VaquitaRedirectToGETResponse(redirect_url,request);
    }
    return new VaquitaRedirectToGETResponse("/login",request);
  }
}
