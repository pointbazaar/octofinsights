package org.vanautrui.octofinsights.controllers.other.tasks;

import org.vanautrui.octofinsights.services.TasksService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.util.Map;

import static java.lang.Integer.parseInt;

public class TaskAddController extends VaquitaController {
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

      int project_id = parseInt(vaquitaHTTPEntityEnclosingRequest.getQueryParameter("project_id"));

      String redirect_url = request.getQueryParameter("redirect");

      Map<String, String> params = vaquitaHTTPEntityEnclosingRequest.getPostParameters();

      TasksService.insertTask(user_id,project_id,params.get("task_name"),parseInt(params.get("effort_estimate")));

      return new VaquitaRedirectToGETResponse(redirect_url,request);
    }
    return new VaquitaRedirectToGETResponse("/login",request);
  }
}
