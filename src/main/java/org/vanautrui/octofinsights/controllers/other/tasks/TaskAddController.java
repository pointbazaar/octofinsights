package org.vanautrui.octofinsights.controllers.other.tasks;

import org.vanautrui.octofinsights.services.TasksService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVPOSTHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;

import java.util.Map;

import static java.lang.Integer.parseInt;

public class TaskAddController implements IVPOSTHandler {

  @Override
  public IVHTTPResponse handlePOST(VHTTPPostRequest req, VApp vApp) throws Exception {
    if( req.session().isPresent()
            && req.session().get().containsKey("authenticated")
            && req.session().get().get("authenticated").equals("true")
            && req.session().get().containsKey("user_id")
    ) {
      final int user_id               = parseInt(req.session().get().get("user_id"));
      final int project_id            = parseInt(req.getQueryParam("project_id"));
      final String redirect_url       = req.getQueryParam("redirect");
      final Map<String,String> params = req.getPostParameters();

      TasksService.insertTask(user_id,project_id,params.get("task_name"),parseInt(params.get("effort_estimate")));

      return new VRedirectToGETResponse(redirect_url,req);
    }
    return new VRedirectToGETResponse("/login",req);
  }
}
