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


    private static ContainerTag makeEffortDisplay(int effort_spent, int effort_estimated){

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
            span("|").withClasses("mr-1","ml-1"),
            span(effort_estimated+"h "),
            span("estimate").withClasses("ml-1")
        ).withClasses("row justify-content-center");
      return res;
    }

    private static ContainerTag makeCompletedTask(Record task){
        return makeCompletedTask(task.get(TASKS.TASK_NAME),task.get(TASKS.ID),task.get(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS),task.get(TASKS.EFFORT_SPENT),task.get(TASKS.PROJECT_ID));
    }

    private static ContainerTag makeCompletedTask(String task_name, int task_id, int effort_estimate, int effort_spent, int project_id){
        //TODO: make that checking or unchecking the checkbox makes a POST request to change the task in the DB
        return tr(

            td(
                    s(task_name)
            ),
            td(effort_spent+""),
            td(effort_estimate+""),
            td(
              form(
                button(
                        "DELETE"
                ).withClasses("btn","btn-sm","btn-outline-danger","m-2","mr-4").withType("submit")
              ).withAction("/tasks/action?id="+task_id+"&action=delete&redirect="+"/projects/view?id="+project_id).withMethod("POST")
            ).withClasses("row","justify-content-end")

        );
    }

    private static ContainerTag makeTask(Record task){
        return makeTask(
                task.get(TASKS.TASK_NAME),task.get(TASKS.ID),task.get(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS),task.get(TASKS.EFFORT_SPENT),task.get(TASKS.PROJECT_ID)
        );
    }

    private static ContainerTag makeTask(String task, int task_id, int effort_estimate, int effort_spent, int project_id){
        return tr(

            td(span(task)),
            td(effort_spent+""),
            td(effort_estimate+""),
            td(
                form(
                        button(
                          "spend 1h"
                        ).withClasses("btn","btn-primary","btn-sm","mr-3").withType("submit")
                ).withAction("/tasks/action?id="+task_id+"&action=spend1hour&redirect="+"/projects/view?id="+project_id).withMethod("POST"),
                form(
                  button(

                  ).withStyle("width:30px; height:30px; border: 3px solid black; border-radius:4px;").withType("submit")
                ).withAction("/tasks/action?id="+task_id+"&action=complete&redirect="+"/projects/view?id="+project_id).withMethod("POST")
            ).withClasses("row","justify-content-end")

        );
    }

    private static ContainerTag makeTasksTable(final List<Record> tasks, final boolean isCompletedTasksTable){
        return
        table(
            thead(
                th("Task").attr("scope","col"),
                th("Effort spent").attr("scope","col"),
                th("Effort estimated").attr("scope","col"),
                th("Actions").attr("scope","col")
            ),
            tbody(
                each(tasks,task->
                    iffElse(isCompletedTasksTable,makeCompletedTask(task),makeTask(task))
                )
            )
        ).withClasses("table","table-sm");
    }

    public static Object get(Request req, Response res) {
        boolean loggedin= req.session().attributes().contains("authenticated") && req.session().attribute("authenticated").equals("true");
        if(!loggedin){
            res.redirect("/login");
            return "";
        }

        int user_id = parseInt(req.session().attribute("user_id"));

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
                                NavigationUtil.createNavbar(req.session().attribute("username"),"Projects"),
                                div(
                                        h3("Project: "+project.get(PROJECTS.PROJECT_NAME)),

                                        div(
                                                div(
                                                        h5("Description"),
                                                        p(project.get(PROJECTS.PROJECT_DESCRIPTION))
                                                ).withClasses("col","mt-3","mb-3","p-3"),
                                                div(
                                                        strong(
                                                                span("Customer: "),
                                                                a(project_customer.get(CUSTOMERS.CUSTOMER_NAME)).withHref("/customers/view?id="+project_customer.get(CUSTOMERS.ID))
                                                        ),
                                                        p(
                                                            span("Project Start Date: "+project.get(PROJECTS.PROJECT_START).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)), br(),
                                                            span("Project End Date Estimate: "+project.get(PROJECTS.PROJECT_END_ESTIMATE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),br(),
                                                            span("Project End Date: "+project.get(PROJECTS.PROJECT_END).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),br(),
                                                            span("Estimated Earnings: "+project.get(PROJECTS.PROJECT_EARNINGS_ESTIMATE)+" $"),br(),
                                                            span("Initial Effort Estimate: "+project.get(PROJECTS.INITIAL_EFFORT_ESTIMATE_HOURS)+" hours")
                                                        )
                                                ).withClasses("col","mt-3","mb-3","p-3")
                                        ).withClasses("row","justify-content-center"),
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
                                                        ).withClasses("col-md-6"),
                                                        div(
                                                                div(
                                                                        //label("Time Estimate (hours)"),
                                                                        input().withType("number").withClasses("form-control").withName("effort_estimate").withPlaceholder("0"),
                                                                        div(span(" hours ").withClasses("input-group-text")).withClasses("input-group-append")
                                                                ).withClasses("input-group")
                                                        ).withClasses("col-md-3"),
                                                        button("ADD TASK").withClasses("btn","btn-primary","btn-block","col-md-3").withType("submit")
                                                ).withClasses("form-row","mt-3","mb-3")

                                        ).withAction("/tasks/add?project_id="+project_id+"&redirect="+"/projects/view?id="+project_id).withMethod("POST"),
                                        div().withClasses("mt-4","mb-4"),
                                        h3("Tasks"),
                                        makeTasksTable(tasks,false),

                                        h3("Completed Tasks").withClasses("mt-3", "mb-1"),
                                        makeTasksTable(tasks,true)
                                ).withClasses("container")
                        )
                ).render();


        res.status(200);
        res.type(ContentType.TEXT_HTML.toString());
        return page;
    }
}
