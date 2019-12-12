package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.UsersService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import spark.Request;
import spark.Response;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public final class ProfileController {

    //https://www.youtube.com/watch?v=o_1aF54DO60&list=RDEMYGj5tu94_mNz6SrYkDD3_g&start_radio=1

    public static Object get(Request req, Response res) {
        if( req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true") ) {

            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            Record user = null;
            try {
                user = UsersService.getUserById(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            ContainerTag page = html(
                    HeadUtil.makeHead(),
                    body(
                            NavigationUtil.createNavbar(req.session().get().get("username"),"Profile"),
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

            res.status(200);
            res.type(ContentType.TEXT_HTML.toString());
            return page.render();
        }else {
            res.redirect("/login");
            return "";
        }
    }
}
