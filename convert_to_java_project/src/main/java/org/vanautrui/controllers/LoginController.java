package org.vanautrui.controllers;

import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.util.Map;

public class LoginController extends VaquitaController {



    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {

        return new VaquitaTextResponse(404,"not implemented");
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        //TODO: verify login credentials and set cookie

        Map<String,String> parameters= vaquitaHTTPEntityEnclosingRequest.getPostParameters();

        if(parameters.get("username").equals("test")){
            if (parameters.get("password").equals("test")){
                return new VaquitaTextResponse(200,"logged in correctly");
            }
        }

        return new VaquitaRedirectResponse("/",vaquitaHTTPEntityEnclosingRequest);
    }
}
