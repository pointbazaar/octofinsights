package org.vanautrui.octofinsights.controllers.other.tasks;

import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.TasksService;
import spark.Request;
import spark.Response;

import java.util.Map;

import static java.lang.Integer.parseInt;

public final class TaskAddController {

  public static Object post(Request req, Response res) {
    if( req.session().attributes().contains("authenticated")
            && req.session().attribute("authenticated").equals("true")
            && req.session().attributes().contains("user_id")
    ) {
      final int user_id               = parseInt(req.session().attribute("user_id"));
      final int project_id            = parseInt(req.queryParams("project_id"));
      final String redirect_url       = req.queryParams("redirect");
      final Map<String,String> params = req.params();

      try {
        TasksService.insertTask(user_id,project_id,params.get("task_name"),parseInt(params.get("effort_estimate")));
      } catch (Exception e) {
        e.printStackTrace();
        res.status(500);
        res.type(ContentType.TEXT_PLAIN.toString());
        return e.getMessage();
      }
      res.redirect(redirect_url);
    }else{
      res.redirect("/login");
    }
    return "";
  }
}
