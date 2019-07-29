package org.vanautrui.octofinsights.controllers.auth;

import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

public class OAuthResponseController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest vaquitaHTTPRequest, VaquitaApp vaquitaApp) throws Exception {

        System.out.println(vaquitaHTTPRequest.getHost());
        System.out.println(vaquitaHTTPRequest.getPath());
        try {
            System.out.println(vaquitaHTTPRequest.getQueryParameter("error"));
        }catch (Exception e){}
        try{
            System.out.println(vaquitaHTTPRequest.getQueryParameter("code"));
        }catch (Exception e){}

        return new VaquitaTextResponse(200,"it worked: ");
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest, VaquitaApp vaquitaApp) throws Exception {
        return null;
    }
}
