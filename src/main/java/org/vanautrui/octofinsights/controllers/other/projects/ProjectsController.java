package org.vanautrui.octofinsights.controllers.other.projects;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ProjectsService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVFullController;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPutRequest;
import org.vanautrui.vaquitamvc.responses.*;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public final class ProjectsController {

    private ContainerTag makeInactiveProjectDiv(String projectName,int project_id){
        ContainerTag res=
                li(
                  div(
                    div(
                            s(projectName)
                    ).withClasses("col-md-6"),
                    div(

                      form(
                        button(
                                "UNARCHIVE"
                        ).withClasses("btn","btn-outline-secondary","m-2")
                      ).withAction("/projects?action=unarchive&id="+project_id).withMethod("POST"),

                      form(
                        button(
                                "DELETE"
                        ).withClasses("btn","btn-outline-danger","m-2")
                      ).withAction("/projects?action=delete&id="+project_id).withMethod("POST")

                    ).withClasses("col-md-6","row","justify-content-end")
                  ).withClasses("row")
                ).withClasses("list-group-item");
        return res;
    }

    private ContainerTag makeProjectDiv(String projectName,int project_id,int user_id) throws Exception {
        ContainerTag res=
                li(
                    div(
                        div(
                            a(
                              projectName
                            ).withHref("/projects/view?id="+project_id),

                            div(
                              div().withClasses("progress-bar").withRole("progressbar").withStyle("width:"+ProjectsService.getCompletionPercentage(project_id,user_id)+"%;")
                            ).withClasses("progress","mt-3").withStyle("height:20px;")
                        ).withClasses("col-md-6"),
                        div(

                            a(
                              button(
                                  "EDIT"
                              ).withClasses("btn","btn-outline-primary","m-2")
                            ).withHref("/projects/edit?id="+project_id),

                            form(
                              button(
                                  "ARCHIVE"
                              ).withClasses("btn","btn-outline-secondary","m-2").withType("submit")
                            ).withAction("/projects?action=archive&id="+project_id).withMethod("POST")
                        ).withClasses("col-md-6","row","justify-content-end")
                    ).withClasses("row")
                ).withClasses("list-group-item");
        return res;
    }

    public static Object get(Request req, Response res) {

        boolean loggedin=req.session().isPresent() && req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true");
        if(!loggedin){
            res.redirect("/login");
            return;
        }

        int user_id = parseInt(req.session().get().get("user_id"));

        Result<Record> myprojects = null;
        try {
            myprojects = ProjectsService.getProjectsByUserId(user_id);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return e.getMessage();
        }
        List<Record> active_projects = myprojects.stream().filter(proj -> proj.get(PROJECTS.ISACTIVE).intValue() == 1).collect(Collectors.toList());
        List<Record> inactive_projects = myprojects.stream().filter(proj -> proj.get(PROJECTS.ISACTIVE).intValue() == 0).collect(Collectors.toList());

        final String page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                NavigationUtil.createNavbar(req.session().get().get("username"),"Projects"),
                                div(
                                        div().withClasses("m-3"),
                                        div(
                                                a(
                                                        button(
                                                                "ADD PROJECT"
                                                        ).withClasses("btn","btn-outline-primary","col-md-12")
                                                ).withHref("/projects/add"),
                                                h5("ACTIVE PROJECTS").withClasses("m-2"),
                                                ul(

                                                        each(
                                                                active_projects,
                                                                proj-> {
                                                                    try {
                                                                        return makeProjectDiv(
                                                                                proj.get(PROJECTS.PROJECT_NAME), proj.get(PROJECTS.ID),user_id
                                                                        );
                                                                    } catch (Exception e) {
                                                                        return p("error making project div");
                                                                    }
                                                                }
                                                        )

                                                ).withClasses("list-group"),
                                                h5("INACTIVE PROJECTS").withClasses("m-2"),
                                                ul(
                                                        each(inactive_projects,proj->makeInactiveProjectDiv(proj.get(PROJECTS.PROJECT_NAME), proj.get(PROJECTS.ID)))

                                                ).withClasses("list-group")
                                        ).withClasses("col-md-12")
                                ).withClasses("container")
                        )
                ).render();

        res.status(200);
        res.type(ContentType.TEXT_HTML.toString());
        return page;
    }

    public static Object post(Request req, Response res) {
        // this method should handle
        // archive, unarchive, delete
        // of projects

        if( req.session().isPresent()
                && req.session().get().containsKey("authenticated")
                && req.session().get().get("authenticated").equals("true")
                && req.session().get().containsKey("user_id")
        ) {
            int user_id = parseInt(req.session().get().get("user_id"));

            final int project_id = parseInt(req.queryParams("id"));

            final String action = req.queryParams("action");

            Connection conn= null;
            try {
                conn = DBUtils.makeDBConnection();
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            byte inactive =0;
            byte active=1;

            switch (action){
                case "archive":
                    System.out.println("try to archive project with id "+project_id);
                    ctx.update(PROJECTS).set(PROJECTS.ISACTIVE,inactive).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).execute();
                    break;
                case "unarchive":
                    System.out.println("try to unarchive project with id "+project_id);
                    ctx.update(PROJECTS).set(PROJECTS.ISACTIVE,active).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).execute();
                    break;
                case "delete":
                    System.out.println("try to delete project with id "+project_id);
                    ctx.delete(PROJECTS).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).execute();
                    break;
                default:
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    res.status(400);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return ("Bad Request, this action is not available.");
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            res.redirect("/projects");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
