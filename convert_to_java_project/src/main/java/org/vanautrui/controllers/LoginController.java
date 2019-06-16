package org.vanautrui.controllers;

import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginController extends VaquitaController {



    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {

        return new VaquitaTextResponse(404,"not implemented GET for logincontroller");
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

        return new VaquitaRedirectResponse("/login.html?message=wrong_password",vaquitaHTTPEntityEnclosingRequest);
    }
}
