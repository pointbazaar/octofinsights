package org.vanautrui.octofinsights.controllers.other.projects;

import org.jooq.Record;
import org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ProjectsService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVFullController;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPutRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public class ProjectEditController implements IVFullController {

  @Override
  public IVHTTPResponse handleGET(VHTTPGetRequest request, VApp app) throws Exception {
    boolean loggedin = request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
    if (!loggedin) {
      return new VRedirectResponse("/login", request, app);
    }

    int user_id = parseInt(request.session().get().get("user_id"));
    int project_id = parseInt(request.getQueryParam("id"));

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
                                                    p("Project Customer:"),
                                                    SalesJ2HTMLUtils.makeCustomerSelect(user_id),

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
    return new VHTMLResponse(200, page);
  }

  @Override
  public IVHTTPResponse handlePOST(VHTTPPostRequest entReq, VApp vApp) throws Exception {

    if( entReq.session().isPresent() && entReq.session().get().containsKey("authenticated")
            && entReq.session().get().get("authenticated").equals("true")
            && entReq.session().get().containsKey("user_id")
    ){
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

      //TODO : handle missing parameters

      Map<String, String> params = entReq.getPostParameters();

      int id = parseInt(entReq.getQueryParam("id"));
      int user_id = parseInt(entReq.session().get().get("user_id"));

      Optional<Integer> customer_id_opt=Optional.empty();
      if(params.containsKey("customer_id")) {
        customer_id_opt = Optional.of(parseInt(params.get("customer_id")));
      }

      Optional<String> project_name=Optional.empty();
      if(params.containsKey("project-name")) {
        project_name = Optional.of(params.get("project-name"));
      }


      Optional<String> project_description = Optional.empty();
      if(params.containsKey("project-description")){
        project_description = Optional.of(params.get("project-description"));
      }

      if(customer_id_opt.isPresent()) {
        ProjectsService.updateProjectCustomer(user_id, id, customer_id_opt.get());
      }
      ProjectsService.updateProject(user_id,id,project_name,project_description);

      return new VRedirectToGETResponse("/projects",entReq);

    }else {
      return new VRedirectToGETResponse("/login",entReq);
    }
  }

  @Override
  public IVHTTPResponse handlePUT(VHTTPPutRequest vhttpPutRequest, VApp vApp) throws Exception {
    return null;
  }
}
