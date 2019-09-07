package org.vanautrui.octofinsights.controllers.other.projects;

import j2html.tags.ContainerTag;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;

import java.sql.Connection;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public class ProjectViewController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

      boolean loggedin=request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
      if(!loggedin){
          return new VaquitaRedirectResponse("/login",request,app);
      }

      int user_id = parseInt(request.session().get().get("user_id"));

      int project_id=Integer.parseInt(request.getQueryParameter("id"));

      Connection conn= DBUtils.makeDBConnection();

      DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
      Record project = ctx.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).fetchOne();

      String page =
        html(
          HeadUtil.makeHead(),
          body(
            NavigationUtil.createNavbar(request.session().get().get("username"),"Projects"),
            div(
              h3("Project: "+project.get(PROJECTS.PROJECT_NAME)),
              hr(),
              div(
                      "TODO: project metadata"
              ),
              form(
                div(
                  div(
                    label("Task"),
                    input().withType("text").withClasses("form-control")
                  ).withClasses("form-group col-md-6"),
                  div(
                    label("Time Estimate (hours)"),
                    input().withType("number").withClasses("form-control")
                  ).withClasses("form-group col-md-6")
                ).withClasses("form-row"),
                button("ADD TASK").withClasses("btn","btn-primary","btn-block").withType("submit")
              ),
              div().withClasses("mt-4","mb-4"),
              h3("Tasks"),
              ul(
                makeTask("example task",-1)
              ).withClasses("list-group"),
              h3("Completed Tasks"),
              ul(
                makeCompletedTask("example task 2",-1)
              ).withClasses("list-group")
            ).withClasses("container")
          )
        ).render();


      return new VaquitaHTMLResponse(200,page);
    }

    private ContainerTag makeEffortDisplay(int effort_spent,int effort_estimated){

      String color;
      int d = effort_spent-effort_estimated;
      if(effort_spent > 2*effort_estimated){
        color="red";
      }else if(effort_spent > effort_estimated){
        color="yellow";
      }else{
        color="green";
      }

      ContainerTag res=
        div(
            span(strong(effort_spent+"h ")).withStyle("color: "+color+";"),
            span("spent").withClasses("ml-1"),
            span(strong("|")).withClasses("mr-1","ml-1"),
            span(strong(effort_estimated+"h ")),
            span("estimate").withClasses("ml-1")
        ).withClasses("row justify-content-center");
      return res;
    }

    private ContainerTag makeCompletedTask(String task_name,int task_id){
        //TODO: make that checking or unchecking the checkbox makes a POST request to change the task in the DB
        ContainerTag res=
                li(
                  div(
                    div(
                            s(strong(task_name))
                    ).withClasses("col-md-6"),
                    div(
                            makeEffortDisplay(3,5)
                    ).withClasses("col-md-2"),
                    div(

                      button(
                              "DELETE"
                      ).withClasses("btn","btn-sm","btn-outline-danger","m-2","mr-4"),

                      div(
                        "✓"
                      ).withStyle("font-size:2em; color:green;")

                    ).withClasses("col-md-4","row","justify-content-end")
                  ).withClasses("row","align-items-center")
                ).withClasses("list-group-item");
        return res;
    }

    private ContainerTag makeTask(String task,int task_id){
        ContainerTag res=
                li(
                    div(
                        div(
                              strong(task)
                        ).withClasses("col-md-6"),
                        div(
                                makeEffortDisplay(13,3)
                        ).withClasses("col-md-2"),
                        div(
                            div(

                            ).withStyle("width:30px; height:30px; border: 3px solid black; border-radius:4px;")

                        ).withClasses("col-md-4","row","justify-content-end")
                    ).withClasses("row","align-items-center")
                ).withClasses("list-group-item");
        return res;
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest request,VaquitaApp app) throws Exception {
      //not implemented
      return null;
    }
}
