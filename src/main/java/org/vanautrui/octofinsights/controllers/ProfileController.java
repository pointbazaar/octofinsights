package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ProfileService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import static j2html.TagCreator.*;

public class ProfileController extends org.vanautrui.vaquitamvc.controller.VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ) {

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            ContainerTag page = html(
                    HeadUtil.makeHead(),
                    body(
                            NavigationUtil.createNavbar(request.session().get().get("username"),"Profile"),
                            div(attrs(".container"),
                                    div(attrs("#main-content"),
                                            p("TODO: make profile page"),
                                            p("Email:"),
                                            p(ProfileService.getEmailById(user_id))
                                    )
                            )
                    )
            );

            return new VaquitaHTMLResponse(200,page.render());
        }else {
            return new VaquitaRedirectToGETResponse("/login", request);
        }
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return null;
    }
}
