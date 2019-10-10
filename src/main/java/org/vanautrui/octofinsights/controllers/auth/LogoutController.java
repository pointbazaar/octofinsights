package org.vanautrui.octofinsights.controllers.auth;

import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;

public class LogoutController implements IVGETHandler {

    @Override
    public IVHTTPResponse handleGET(VHTTPGetRequest request, VApp vApp) throws Exception {
        if( request.session().isPresent()
                && request.session().get().containsKey("authenticated")
                && request.session().get().get("authenticated").equals("true") ) {
            request.session().get().remove("authenticated");
        }

        return new VRedirectToGETResponse("/",request);
    }
}
