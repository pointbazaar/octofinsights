package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.*;
import org.vanautrui.octofinsights.controllers.api.*;
import org.vanautrui.octofinsights.controllers.auth.*;
import org.vanautrui.octofinsights.controllers.other.expenses.ExpensesController;
import org.vanautrui.octofinsights.controllers.other.expenses.ExpensesEditController;
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
        routes.put("/expenses/edit",new ExpensesEditController());

        routes.put("/invoices",new InvoicesController());

        routes.put("/profile",new ProfileController());

        routes.put("/login",new LoginController());
        routes.put("/logout",new LogoutController());

        routes.put("/oauth_login",new LoginWithOAuthViaGoogleController());
        routes.put("/auth",new OAuthResponseController());

        routes.put("/register",new RegisterController());



        routes.put("/api/cashflow",new CashFlowEndpoint());
        routes.put("/api/businessvaluehistory",new BusinessValueHistoryEndpoint());

        routes.put("/api/value",new BalanceEndpoint());
        routes.put("/api/salesthismonth",new SalesThisMonthEndpoint());
        routes.put("/api/expensesthismonth",new ExpensesThisMonthEndpoint());
        routes.put("/api/profit",new ProfitEndpoint());

        try {
            VaquitaApp app = new VaquitaApp(9377, routes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
