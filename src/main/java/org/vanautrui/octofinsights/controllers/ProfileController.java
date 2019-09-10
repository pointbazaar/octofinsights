package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.UsersService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public class ProfileController extends org.vanautrui.vaquitamvc.controller.VaquitaController {

    //https://www.youtube.com/watch?v=o_1aF54DO60&list=RDEMYGj5tu94_mNz6SrYkDD3_g&start_radio=1

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ) {

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            Record user = UsersService.getUserById(user_id);

            ContainerTag page = html(
                    HeadUtil.makeHead(),
                    body(
                        NavigationUtil.createNavbar(request.session().get().get("username"),"Profile"),
                        div(
                            div(
                                p(
                                        span("Id: "),
                                        strong(user.get(USERS.ID)+"")
                                ),
                                p(
                                        span("User: "), strong(user.get(USERS.USERNAME))
                                ),
                                p(
                                        span("Email: "), strong(user.get(USERS.EMAIL))
                                )
                            ).withId("main-content")
                        ).withClasses("container")
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
