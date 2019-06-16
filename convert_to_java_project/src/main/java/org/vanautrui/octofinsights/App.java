package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.IndexController;
import org.vanautrui.octofinsights.controllers.LeadsController;
import org.vanautrui.octofinsights.controllers.LoginController;
import org.vanautrui.octofinsights.controllers.api.CashFlowEndpoint;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;

import java.util.HashMap;
import java.util.Map;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        Map<String, VaquitaController> routes=new HashMap<String, VaquitaController>();

        routes.put("/", new IndexController());
        routes.put("/leads",new LeadsController());

        routes.put("/login",new LoginController());

        routes.put("/api/cashflow",new CashFlowEndpoint());

        try {
            VaquitaApp app = new VaquitaApp(8080, routes, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
