package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.*;
import org.vanautrui.octofinsights.controllers.api.CashFlowEndpoint;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;

import java.util.HashMap;
import java.util.Map;

public class App
{

    public static String octofinsights_primary_color = "#ffc143";

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        Map<String, VaquitaController> routes=new HashMap<String, VaquitaController>();

        routes.put("/", new IndexController());

        routes.put("/dashboard",new DashboardController());
        routes.put("/leads",new LeadsController());
        routes.put("/sales",new SalesController());

        routes.put("/login",new LoginController());
        routes.put("/logout",new LogoutController());

        routes.put("/register",new RegisterController());

        routes.put("/api/cashflow",new CashFlowEndpoint());

        try {
            VaquitaApp app = new VaquitaApp(8080, routes, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
