package org.vanautrui.octofinsights.controllers.other.projects;

import org.apache.http.entity.ContentType;
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
import spark.Request;
import spark.Response;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;

public final class ProjectAddController {

    public static Object post(Request req, Response res) {
        if( entReq.session().get().containsKey("authenticated")
                && entReq.session().get().get("authenticated").equals("true")
                && entReq.session().get().containsKey("user_id")
        ){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Map<String, String> params = entReq.getPostParameters();

            int user_id = parseInt(req.session().attribute("user_id"));

            String project_name = params.get("project-name");

            String s1 = params.get("project-start-date");
            String s2 = params.get("project-end-date-estimate");

            Timestamp project_start_date = new Timestamp(dateFormat.parse(s1).getTime());
            Timestamp project_end_date_estimate = new Timestamp(dateFormat.parse(s2).getTime());

            int effort_estimate_hours = parseInt(params.get("project-effort-estimate"));
            int earnings_estimate = parseInt(params.get("project-earnings-estimate"));
            String project_description = params.get("project-description");

            int customer_id = parseInt(params.get("customer_id"));

            try {
                ProjectsService.insertProject(user_id,project_name,project_start_date,project_end_date_estimate,effort_estimate_hours,earnings_estimate,project_description,customer_id);
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

    public static Object get(Request req, Response res) {
        boolean loggedin = req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true");
        if (!loggedin) {
            res.redirect("/login");
            return "";
        }

        int user_id = parseInt(req.session().get().get("user_id"));

        //TODO: create integrity constrains on the mysql database

        String page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                NavigationUtil.createNavbar(req.session().get().get("username"), "Projects"),
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

        res.status(200);
        res.type(ContentType.TEXT_HTML.toString());
        return page;
    }
}
