package org.vanautrui.octofinsights.controllers.other.projects;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ProjectsService;
import spark.Request;
import spark.Response;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public final class ProjectEditController  {

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            //TODO : handle missing parameters

            Map<String, String> params = req.params();

            int id = parseInt(req.queryParams("id"));
            int user_id = parseInt(req.session().attribute("user_id"));

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
                try {
                    ProjectsService.updateProjectCustomer(user_id, id, customer_id_opt.get());
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }
            try {
                ProjectsService.updateProject(user_id,id,project_name,project_description);
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
