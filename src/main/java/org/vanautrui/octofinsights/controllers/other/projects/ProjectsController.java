package org.vanautrui.octofinsights.controllers.other.projects;

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
                                            button(
                                                    "ADD PROJECT"
                                            ).withClasses("btn","btn-outline-primary","col-md-12"),
                                            h5("ACTIVE PROJECTS").withClasses("m-2"),
                                            ul(
                                                li(
                                                        "Project 1"
                                                ).withClasses("list-group-item")
                                            ).withClasses("list-group"),
                                            h5("INACTIVE PROJECTS").withClasses("m-2"),
                                            ul(
                                                li(s(
                                                        "Project 1")
                                                ).withClasses("list-group-item")
                                            ).withClasses("list-group")
                                        ).withClasses("col-md-12")
                                ).withClasses("cointainer")
                        )
                ).render();
        return new VaquitaHTMLResponse(200,page);
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return new VaquitaTextResponse(200,"not yet implemnted");
    }
}
