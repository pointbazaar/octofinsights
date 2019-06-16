package org.vanautrui;

import org.vanautrui.controllers.IndexController;
import org.vanautrui.controllers.LoginController;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.controller.VaquitaTextController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.util.HashMap;
import java.util.Map;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        Map<String, VaquitaController> routes=new HashMap<String, VaquitaController>();

        routes.put("/", new IndexController());

        routes.put("/login",new LoginController());

        try {
            VaquitaApp app = new VaquitaApp(8080, routes, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
