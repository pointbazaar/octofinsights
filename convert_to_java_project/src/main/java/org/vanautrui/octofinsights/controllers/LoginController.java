package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.util.Map;

import static j2html.TagCreator.*;

public class LoginController extends VaquitaController {



    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {

        ContainerTag page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                div(
                                        attrs(".container"),
                                        h1("Login"),
                                        form(
                                                label("Email"),
                                                input().withName("username").withPlaceholder("username").withValue("test").withType("text"),
                                                label("Password"),
                                                input().withName("password").withPlaceholder("password").withValue("test").withType("password"),
                                                label("Business"),
                                                input().withName("business").withPlaceholder("business").withValue("test").withType("text"),
                                                button(attrs(".btn .btn-primary"),"Login").withType("submit")
                                        ).withAction("/login").withMethod("post"),
                                        p(
                                                "test credentials: 'test','test','vanautrui'"
                                        )
                                )
                        )
                );

        return new VaquitaHTMLResponse(200,page.render());
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        //TODO: verify login credentials and set cookie

        Map<String,String> parameters= vaquitaHTTPEntityEnclosingRequest.getPostParameters();

        if(parameters.get("username").equals("test")){
            if (parameters.get("password").equals("test")){
                if(vaquitaHTTPEntityEnclosingRequest.session().isPresent()){
                    vaquitaHTTPEntityEnclosingRequest.session().get().put("authenticated","true");
                    vaquitaHTTPEntityEnclosingRequest.session().get().put("username",parameters.get("username"));

                    return new VaquitaHTMLResponse(200,"<html><a href='/'>successfully logged in. click here.</a></html>");
                }else{
                    return new VaquitaTextResponse(500,"something went wrong with the sessions");
                }
            }
        }

        return new VaquitaRedirectResponse("/login?message=wrong_password",vaquitaHTTPEntityEnclosingRequest);
    }
}
