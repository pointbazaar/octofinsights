package org.vanautrui.octofinsights.controllers.auth;

import spark.Request;
import spark.Response;

public final class LogoutController  {

    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("authenticated") ) {

            req.session().removeAttribute("authenticated");

        }

        res.redirect("/");

        return "";
    }
}
