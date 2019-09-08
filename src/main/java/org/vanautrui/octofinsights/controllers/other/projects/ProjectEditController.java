package org.vanautrui.octofinsights.controllers.other.projects;

import org.jooq.Record;
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

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public class ProjectEditController extends VaquitaController {

  @Override
  public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
    boolean loggedin = request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
    if (!loggedin) {
      return new VaquitaRedirectResponse("/login", request, app);
    }

    int user_id = parseInt(request.session().get().get("user_id"));
    int project_id = parseInt(request.getQueryParameter("id"));

    Record project = ProjectsService.getById(user_id,project_id);

    String page =
      html(
        HeadUtil.makeHead(),
        body(
          NavigationUtil.createNavbar(request.session().get().get("username"), "Projects"),
          div(
            h3("Edit Project").withClasses("text-center"),
            hr(),
            div(
              form(
                div(
                  label("Project Name"),
                  input().withType("text").withClasses("form-control")
                          .withName("project-name").withPlaceholder("my new project")
                          .withValue(project.get(PROJECTS.PROJECT_NAME))
                ).withClasses("form-group"),

                div(
                  label("Project Description"),
                  textarea(
                          project.get(PROJECTS.PROJECT_DESCRIPTION)
                  ).withClasses("form-control")
                          .withPlaceholder("tasks, goals, stakeholders, technologies, deadlines, links to resources, ...")
                          .withName("project-description")
                ).withClasses("form-group"),

                button(
                        "Save Project"
                ).withClasses("btn", "btn-primary", "col-md-12")
                        .withType("submit")
              ).withAction("/projects/edit?id="+project_id).withMethod("POST")

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

      //TODO : handle missing parameters

      Map<String, String> params = entReq.getPostParameters();

      int id = parseInt(req.getQueryParameter("id"));
      int user_id = parseInt(req.session().get().get("user_id"));

      Optional<String> project_name=Optional.empty();
      if(params.containsKey("project-name")) {
         project_name = Optional.of(params.get("project-name"));
      }


      Optional<String> project_description = Optional.empty();
      if(params.containsKey("project-description")){
        project_description = Optional.of(params.get("project-description"));
      }

      ProjectsService.updateProject(user_id,id,project_name,project_description);

      return new VaquitaRedirectToGETResponse("/projects",req);

    }else {
      return new VaquitaRedirectToGETResponse("/login",req);
    }
  }
}
