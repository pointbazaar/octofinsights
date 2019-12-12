package org.vanautrui.octofinsights.controllers.auth;

import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import spark.Request;
import spark.Response;

public final class LogoutController  {

    public static Object get(Request req, Response res) {
        if( request.session().isPresent()
                && request.session().get().containsKey("authenticated")
                && request.session().get().get("authenticated").equals("true") ) {

            request.session().get().remove("authenticated");
        }else {
            res.redirect("/");
        }
        return "";
    }
}
