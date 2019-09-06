package org.vanautrui.octofinsights.controllers.other.projects;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import static j2html.TagCreator.*;

public class ProjectsController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
        boolean loggedin=request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true");
        if(!loggedin){
            return new VaquitaRedirectResponse("/login",request,app);
        }

        int user_id = Integer.parseInt(request.session().get().get("user_id"));

        //TODO: get all the projects

        String page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                NavigationUtil.createNavbar(request.session().get().get("username"),"Projects"),
                                div(
                                        div(
                                            a(
                                              button(
                                                    "ADD PROJECT"
                                              ).withClasses("btn","btn-outline-primary","col-md-12")
                                            ).withHref("/projects/add"),
                                            h5("ACTIVE PROJECTS").withClasses("m-2"),
                                            ul(
                                                makeProjectDiv("Project 1"),
                                                makeProjectDiv("Project 2")
                                            ).withClasses("list-group"),
                                            h5("INACTIVE PROJECTS").withClasses("m-2"),
                                            ul(
                                                makeInactiveProjectDiv("Project 3")
                                            ).withClasses("list-group")
                                        ).withClasses("col-md-12")
                                ).withClasses("cointainer")
                        )
                ).render();
        return new VaquitaHTMLResponse(200,page);
    }

    private ContainerTag makeInactiveProjectDiv(String projectName){
        ContainerTag res=
                li(
                        div(
                                div(
                                        s(projectName)
                                ).withClasses("col-md-6"),
                                div(
                                        div(
                                                "UNARCHIVE"
                                        ).withClasses("btn","btn-outline-secondary","m-2"),
                                        div(
                                                "DELETE"
                                        ).withClasses("btn","btn-outline-danger","m-2")
                                ).withClasses("col-md-6","row","justify-content-end")
                        ).withClasses("row")
                ).withClasses("list-group-item");
        return res;
    }

    private ContainerTag makeProjectDiv(String projectName){
        ContainerTag res=
                li(
                    div(
                        div(
                            projectName
                        ).withClasses("col-md-6"),
                        div(
                            div(
                                "EDIT"
                            ).withClasses("btn","btn-outline-primary","m-2"),
                            div(
                                "ARCHIVE"
                            ).withClasses("btn","btn-outline-secondary","m-2")
                        ).withClasses("col-md-6","row","justify-content-end")
                    ).withClasses("row")
                ).withClasses("list-group-item");
        return res;
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return new VaquitaTextResponse(200,"not yet implemnted");
    }
}
