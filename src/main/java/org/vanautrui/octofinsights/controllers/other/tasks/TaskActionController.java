package org.vanautrui.octofinsights.controllers.other.tasks;

import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.TasksService;
import spark.Request;
import spark.Response;

import static java.lang.Integer.parseInt;

public final class TaskActionController {

  public static Object post(Request req, Response res) {
    if(        req.session().attributes().contains("authenticated")
            && req.session().attribute("authenticated").equals("true")
            && req.session().attributes().contains("user_id")
    ) {
      final int user_id = parseInt(req.session().attribute("user_id"));

      final String action = req.queryParams("action");
      final int id = parseInt(req.queryParams("id"));

      final String redirect_url = req.queryParams("redirect");

      try {
        switch (action) {
          case "complete":
            TasksService.completeTask(id, user_id);
            break;
          case "spend1hour":
            TasksService.spend1hour(id, user_id);
            break;
          case "delete":
            TasksService.deleteTask(id, user_id);
            break;
          default:
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return ("unrecognized query parameter value");
        }
      }catch (Exception e){
        e.printStackTrace();
        res.status(500);
        res.type(ContentType.TEXT_PLAIN.toString());
        return e.getMessage();
      }

      res.redirect(redirect_url);
    }
    res.redirect("/login");
    return "";
  }
}
