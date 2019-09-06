package org.vanautrui.octofinsights.controllers.other.projects;

import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import static j2html.TagCreator.*;

public class ProjectAddController extends VaquitaController {

  @Override
  public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
    boolean loggedin = request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
    if (!loggedin) {
      return new VaquitaRedirectResponse("/login", request, app);
    }

    int user_id = Integer.parseInt(request.session().get().get("user_id"));


    String page =
      html(
        HeadUtil.makeHead(),
        body(
          NavigationUtil.createNavbar(request.session().get().get("username"), "Projects"),
          div(
            h3("Create a new Project").withClasses("text-center"),
            hr(),
            div(
              form(
                div(
                  label("Project Name"),
                  input().withType("text").withClasses("form-control").withName("project-name")
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
                ).withClasses("form-row"),

                div(
                  div(
                    label("Estimated Effort (hours)"),
                    input().withType("number").withClasses("form-control").withName("project-effort-estimate")
                  ).withClasses("col"),
                  div(
                    label("Estimated Project Earnings (in Euros)"),
                    input().withType("number").withClasses("form-control").withName("project-earnings-estimate")
                  ).withClasses("col")
                ).withClasses("form-row"),

                div(
                  label("Project Description"),
                  textarea(

                  ).withClasses("form-control")
                ).withClasses("form-group")
              ),
              button(
              "ADD PROJECT"
              ).withClasses("btn", "btn-primary", "col-md-12")
            ).withClasses("col-md-12")
          ).withClasses("cointainer")
        )
      ).render();
    return new VaquitaHTMLResponse(200, page);
  }

  @Override
  public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest, VaquitaApp app) throws Exception {
    return new VaquitaTextResponse(200, "not yet implemnted");
  }
}
