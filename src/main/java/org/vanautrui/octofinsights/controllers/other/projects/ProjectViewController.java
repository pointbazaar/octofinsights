package org.vanautrui.octofinsights.controllers.other.projects;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.octofinsights.services.TasksService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectResponse;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;
import static org.vanautrui.octofinsights.generated.tables.Tasks.TASKS;

public final class ProjectViewController {


    private ContainerTag makeEffortDisplay(int effort_spent,int effort_estimated){

      String color;
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

    private ContainerTag makeCompletedTask(String task_name,int task_id,int effort_estimate,int effort_spent,int project_id){
        //TODO: make that checking or unchecking the checkbox makes a POST request to change the task in the DB
        return li(
          div(
            div(
                    s(strong(task_name))
            ).withClasses("col-md-6"),
            div(
                    makeEffortDisplay(effort_spent,effort_estimate)
            ).withClasses("col-md-2"),
            div(
              form(
                button(
                        "DELETE"
                ).withClasses("btn","btn-sm","btn-outline-danger","m-2","mr-4").withType("submit")
              ).withAction("/tasks/action?id="+task_id+"&action=delete&redirect="+"/projects/view?id="+project_id).withMethod("POST"),

              div(
                "✓"
              ).withStyle("font-size:2em; color:green;")

            ).withClasses("col-md-4","row","justify-content-end")
          ).withClasses("row","align-items-center")
        ).withClasses("list-group-item");
    }

    private ContainerTag makeTask(String task,int task_id,int effort_estimate,int effort_spent,int project_id){
        return li(
            div(
                div(
                      strong(task)
                ).withClasses("col-md-6"),
                div(
                        makeEffortDisplay(effort_spent,effort_estimate)
                ).withClasses("col-md-2"),

                div(
                    form(
                            button(
                              "spend 1h"
                            ).withClasses("btn","btn-primary","btn-sm","mr-3").withType("submit")
                    ).withAction("/tasks/action?id="+task_id+"&action=spend1hour&redirect="+"/projects/view?id="+project_id).withMethod("POST"),
                    form(
                      button(

                      ).withStyle("width:30px; height:30px; border: 3px solid black; border-radius:4px;").withType("submit")
                    ).withAction("/tasks/action?id="+task_id+"&action=complete&redirect="+"/projects/view?id="+project_id).withMethod("POST")
                ).withClasses("col-md-4","row","justify-content-end")

            ).withClasses("row","align-items-center")
        ).withClasses("list-group-item");
    }

    public static Object get(Request req, Response res) {
        boolean loggedin= req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true");
        if(!loggedin){
            res.redirect("/login");
            return "";
        }

        int user_id = parseInt(req.session().get().get("user_id"));

        int project_id=Integer.parseInt(req.queryParams("id"));

        Connection conn= null;
        try {
            conn = DBUtils.makeDBConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        Record project = ctx.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).fetchOne();
        byte not_complete = 0;
        byte complete = 1;

        final List<Record> tasks;
        final List<Record> tasks_complete;
        final Record project_customer;
        try {
            tasks = TasksService.getTasksByUserIdAndProjectId(user_id, project_id).stream().filter(task -> task.get(TASKS.ISCOMPLETED) == not_complete).collect(Collectors.toList());
            tasks_complete = TasksService.getTasksByUserIdAndProjectId(user_id, project_id).stream().filter(task -> task.get(TASKS.ISCOMPLETED) == complete).collect(Collectors.toList());

            project_customer = CustomersService.getCustomerById(user_id, project.get(PROJECTS.CUSTOMER_ID));
        }catch (Exception e){
            e.printStackTrace();
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return e.getMessage();
        }

        String page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                NavigationUtil.createNavbar(req.session().get().get("username"),"Projects"),
                                div(
                                        h3("Project: "+project.get(PROJECTS.PROJECT_NAME)),

                                        div(
                                                div(
                                                        h5("Description"),
                                                        p(project.get(PROJECTS.PROJECT_DESCRIPTION))
                                                ).withClasses("card","mt-3","mb-3","p-3"),
                                                div(
                                                        strong(
                                                                span("Customer: "),
                                                                a(project_customer.get(CUSTOMERS.CUSTOMER_NAME)).withHref("/customers/view?id="+project_customer.get(CUSTOMERS.ID))
                                                        ),
                                                        p("Project Start Date: "+project.get(PROJECTS.PROJECT_START).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),
                                                        p("Project End Date Estimate: "+project.get(PROJECTS.PROJECT_END_ESTIMATE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),
                                                        p("Project End Date: "+project.get(PROJECTS.PROJECT_END).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),
                                                        p("Estimated Earnings: "+project.get(PROJECTS.PROJECT_EARNINGS_ESTIMATE)+" $"),
                                                        p("Initial Effort Estimate: "+project.get(PROJECTS.INITIAL_EFFORT_ESTIMATE_HOURS)+" hours")
                                                ).withClasses("card","mt-3","mb-3","p-3")
                                        ),
                                        a(
                                                button(
                                                        "Edit Project"
                                                ).withClasses("btn","btn-outline-secondary")
                                        ).withHref("/projects/edit?id="+project_id),
                                        hr(),
                                        form(
                                                strong("Add Task"),
                                                div(
                                                        div(
                                                                div(
                                                                        //label("Task"),
                                                                        input().withType("text").withClasses("form-control").withName("task_name").withPlaceholder("Task")
                                                                ).withClasses("input-group")
                                                        ).withClasses("col-md-9"),
                                                        div(
                                                                div(
                                                                        //label("Time Estimate (hours)"),
                                                                        input().withType("number").withClasses("form-control").withName("effort_estimate").withPlaceholder("0"),
                                                                        div(span(" hours ").withClasses("input-group-text")).withClasses("input-group-append")
                                                                ).withClasses("input-group")
                                                        ).withClasses("col-md-3")
                                                ).withClasses("form-row","mt-3","mb-3"),
                                                button("ADD TASK").withClasses("btn","btn-primary","btn-block").withType("submit")
                                        ).withAction("/tasks/add?project_id="+project_id+"&redirect="+"/projects/view?id="+project_id).withMethod("POST"),
                                        div().withClasses("mt-4","mb-4"),
                                        h3("Tasks"),
                                        ul(
                                                each(
                                                        tasks,
                                                        task->makeTask(
                                                                task.get(TASKS.TASK_NAME),task.get(TASKS.ID),task.get(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS),task.get(TASKS.EFFORT_SPENT),project_id
                                                        )
                                                )
                                        ).withClasses("list-group"),
                                        h3("Completed Tasks").withClasses("mt-3", "mb-1"),
                                        ul(
                                                each(
                                                        tasks_complete,
                                                        task->makeCompletedTask(
                                                                task.get(TASKS.TASK_NAME),task.get(TASKS.ID),task.get(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS),task.get(TASKS.EFFORT_SPENT),project_id
                                                        )
                                                )
                                        ).withClasses("list-group")
                                ).withClasses("container")
                        )
                ).render();


        res.status(200);
        res.type(ContentType.TEXT_HTML.toString());
        return page;
    }
}
