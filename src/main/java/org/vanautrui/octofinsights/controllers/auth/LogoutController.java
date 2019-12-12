package org.vanautrui.octofinsights.controllers.auth;

import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import spark.Request;
import spark.Response;

public final class LogoutController  {

    public static Response get(Request request, Response response) {
        if( request.session().isPresent()
                && request.session().get().containsKey("authenticated")
                && request.session().get().get("authenticated").equals("true") ) {
            request.session().get().remove("authenticated");
        }

        return new VRedirectToGETResponse("/",request);
    }
}
