package org.vanautrui.octofinsights.controllers.other.projects;

import j2html.tags.ContainerTag;
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
import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public class ProjectsController implements IVFullController {

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

    public static Response get(Request request, Response response) {

        boolean loggedin=request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
        if(!loggedin){
            return new VRedirectResponse("/login",request,app);
        }

        int user_id = parseInt(request.session().get().get("user_id"));

        Result<Record> myprojects = ProjectsService.getProjectsByUserId(user_id);
        List<Record> active_projects = myprojects.stream().filter(proj -> proj.get(PROJECTS.ISACTIVE).intValue() == 1).collect(Collectors.toList());
        List<Record> inactive_projects = myprojects.stream().filter(proj -> proj.get(PROJECTS.ISACTIVE).intValue() == 0).collect(Collectors.toList());

        final String page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                NavigationUtil.createNavbar(request.session().get().get("username"),"Projects"),
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
        return new VHTMLResponse(200,page);
    }

    public static Response post(Request request, Response response) {
        // this method should handle
        // archive, unarchive, delete
        // of projects

        if( request.session().isPresent()
                && request.session().get().containsKey("authenticated")
                && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ) {
            int user_id = parseInt(request.session().get().get("user_id"));

            int project_id = parseInt(request.getQueryParam("id"));

            String action = request.getQueryParam("action");

            Connection conn= DBUtils.makeDBConnection();

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
                    conn.close();
                    return new VTextResponse(400,"BAD REQUEST. this action is not available. ");
            }
            conn.close();
            return new VRedirectToGETResponse("/projects",request);
        }else {
            return new VRedirectToGETResponse("/login", request);
        }
    }
}
