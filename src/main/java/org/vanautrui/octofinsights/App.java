package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.*;
import org.vanautrui.octofinsights.controllers.api.BalanceEndpoint;
import org.vanautrui.octofinsights.controllers.api.CashFlowEndpoint;
import org.vanautrui.octofinsights.controllers.auth.LoginController;
import org.vanautrui.octofinsights.controllers.auth.LogoutController;
import org.vanautrui.octofinsights.controllers.auth.RegisterController;
import org.vanautrui.octofinsights.controllers.other.expenses.ExpensesController;
import org.vanautrui.octofinsights.controllers.other.leads.LeadsController;
import org.vanautrui.octofinsights.controllers.other.sales.SalesController;
import org.vanautrui.octofinsights.controllers.other.sales.SalesEditController;
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
        routes.put("/sales/edit",new SalesEditController());



        routes.put("/expenses",new ExpensesController());

        routes.put("/invoices",new InvoicesController());

        routes.put("/login",new LoginController());
        routes.put("/logout",new LogoutController());

        routes.put("/register",new RegisterController());



        routes.put("/api/cashflow",new CashFlowEndpoint());
        routes.put("/api/balance",new BalanceEndpoint());

        try {
            VaquitaApp app = new VaquitaApp(9377, routes, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
