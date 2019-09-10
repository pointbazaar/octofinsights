package org.vanautrui.octofinsights.controllers.other.projects;

import org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ProjectsService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;

public class ProjectAddController extends VaquitaController {

  @Override
  public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
    boolean loggedin = request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
    if (!loggedin) {
      return new VaquitaRedirectResponse("/login", request, app);
    }

    int user_id = parseInt(request.session().get().get("user_id"));

    //TODO: create integrity constrains on the mysql database

    String page =
      html(
        HeadUtil.makeHead(),
        body(
          NavigationUtil.createNavbar(request.session().get().get("username"), "Projects"),
          div(
            h3("Create a new Project").withClasses("text-center","m-3"),
            hr(),
            div(
              form(
                p("Project Customer:"),
                SalesJ2HTMLUtils.makeCustomerSelect(user_id),
                div(
                  label("Project Name"),
                  input().withType("text").withClasses("form-control").withName("project-name").withPlaceholder("my new project")
                ).withClasses("form-group"),
                div(
                  div(
                    label("Project Start Date"),
                    input().withType("date").withClasses("form-control").withName("project-start-date")
                  ).withClasses("col"),
                  div(
                    label("Projected Project End Date"),
                    input().withType("date").withClasses("form-control").withName("project-end-date-estimate")
                  ).withClasses("col")
                ).withClasses("form-row","mt-3"),

                div(
                  div(
                    label("Estimated Effort (hours)"),
                    input().withType("number").withClasses("form-control").withName("project-effort-estimate")
                  ).withClasses("col"),
                  div(
                    label("Estimated Project Earnings (in Euros)"),
                    input().withType("number").withClasses("form-control").withName("project-earnings-estimate")
                  ).withClasses("col")
                ).withClasses("form-row","mt-3"),

                div(
                  label("Project Description"),
                  textarea(

                  ).withClasses("form-control").withPlaceholder("tasks, goals, stakeholders, technologies, deadlines, links to resources, ...").withName("project-description")
                ).withClasses("form-group"),

                button(
                        "ADD PROJECT"
                ).withClasses("btn", "btn-primary", "col-md-12").withType("submit")
              ).withAction("/projects/add").withMethod("POST")

            ).withClasses("col-md-12")
          ).withClasses("container")
        )
      ).render();
    return new VaquitaHTMLResponse(200, page);
  }

  @Override
  public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest entReq, VaquitaApp app) throws Exception {

    IVaquitaHTTPRequest req = entReq;
    if( req.session().isPresent() && req.session().get().containsKey("authenticated")
            && req.session().get().get("authenticated").equals("true")
            && req.session().get().containsKey("user_id")
    ){
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

      Map<String, String> params = entReq.getPostParameters();

      int user_id = parseInt(req.session().get().get("user_id"));

      String project_name = params.get("project-name");

      String s1 = params.get("project-start-date");
      String s2 = params.get("project-end-date-estimate");

      Timestamp project_start_date = new Timestamp(dateFormat.parse(s1).getTime());
      Timestamp project_end_date_estimate = new Timestamp(dateFormat.parse(s2).getTime());

      int effort_estimate_hours = parseInt(params.get("project-effort-estimate"));
      int earnings_estimate = parseInt(params.get("project-earnings-estimate"));
      String project_description = params.get("project-description");

      int customer_id = parseInt(params.get("customer_id"));

      ProjectsService.insertProject(user_id,project_name,project_start_date,project_end_date_estimate,effort_estimate_hours,earnings_estimate,project_description,customer_id);

      return new VaquitaRedirectToGETResponse("/projects",req);

    }else {
      return new VaquitaRedirectToGETResponse("/login",req);
    }
  }
}
