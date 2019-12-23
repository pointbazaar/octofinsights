package org.vanautrui.octofinsights.controllers.other.projects;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ProjectsService;
import spark.Request;
import spark.Response;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public final class ProjectEditController  {

    private final static SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd");


    public static Object get(Request req, Response res) {

        final boolean loggedin =  req.session().attributes().contains("authenticated") && req.session().attribute("authenticated").equals("true");
        if (!loggedin) {
            res.redirect("/login");
            return "";
        }

        final int user_id = parseInt(req.session().attribute("user_id"));
        final int project_id = parseInt(req.queryParams("id"));

        final Record project;
        try {
            project = ProjectsService.getById(user_id,project_id);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return e.getMessage();
        }

        final String page;
        try {
            page = html(
                    HeadUtil.makeHead(),
                    body(
                            NavigationUtil.createNavbar(req.session().attribute("username"), "Projects"),
                            div(
                                    h3("Edit Project").withClasses("text-center"),
                                    hr(),
                                    div(
                                            form(
                                                    p("Project Customer:"),
                                                    SalesJ2HTMLUtils.makeCustomerSelect(user_id,project.get(PROJECTS.CUSTOMER_ID)),

                                                    div(
                                                            label("Project Name"),
                                                            input().withType("text").withClasses("form-control")
                                                                    .withName("project-name").withPlaceholder("my new project")
                                                                    .withValue(project.get(PROJECTS.PROJECT_NAME))
                                                    ).withClasses("form-group"),

                                                    div(
                                                            label("Project End Date"),
                                                            input().withType("date")
                                                                    .withClasses("form-control")
                                                                    .withName("project-end-date")
                                                                    .withValue(format.format(project.get(PROJECTS.PROJECT_END)))
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
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }


        res.status(200);
        res.type(ContentType.TEXT_HTML.toString());
        return page;
    }

    public static Object post(Request req, Response res) {

        if( req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){
            final int id = parseInt(req.queryParams("id"));
            final int user_id = parseInt(req.session().attribute("user_id"));

            final int customer_id= parseInt(req.queryParams("customer_id"));

            final String project_name= req.queryParams("project-name");


            try {
                final Date new_end_date = format.parse(req.queryParams("project-end-date"));
                final String project_description = req.queryParams("project-description");

                ProjectsService.updateProject(id,user_id,customer_id,project_name,new_end_date,project_description);

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            res.redirect("/projects");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
