package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.DashboardController;
import org.vanautrui.octofinsights.controllers.IndexController;
import org.vanautrui.octofinsights.controllers.InvoicesController;
import org.vanautrui.octofinsights.controllers.ProfileController;
import org.vanautrui.octofinsights.controllers.api.*;
import org.vanautrui.octofinsights.controllers.auth.*;
import org.vanautrui.octofinsights.controllers.other.expenses.ExpensesController;
import org.vanautrui.octofinsights.controllers.other.expenses.ExpensesEditController;
import org.vanautrui.octofinsights.controllers.other.leads.LeadsController;
import org.vanautrui.octofinsights.controllers.other.projects.ProjectAddController;
import org.vanautrui.octofinsights.controllers.other.projects.ProjectEditController;
import org.vanautrui.octofinsights.controllers.other.projects.ProjectViewController;
import org.vanautrui.octofinsights.controllers.other.projects.ProjectsController;
import org.vanautrui.octofinsights.controllers.other.sales.SalesController;
import org.vanautrui.octofinsights.controllers.other.sales.SalesEditController;
import org.vanautrui.octofinsights.controllers.other.tasks.TaskActionController;
import org.vanautrui.octofinsights.controllers.other.tasks.TaskAddController;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App
{

    public static String octofinsights_primary_color = "#ffc143";

    public static String yandex_api_key;
    public static String yandex_translate_base_url ="https://translate.yandex.net/api/v1.5/tr.json/translate";

    private static void setup_translation_configuration() throws Exception{

        List<String> lines= Files.readAllLines(Paths.get("yandex-translate-key.txt"));
        yandex_api_key=lines.get(0);
    }

    public static void main( String[] args )
    {
        System.out.println( "Octofinsights starting ... " );

        try {
            setup_translation_configuration();
        }catch (Exception e){
            e.printStackTrace();
        }

        Map<String, VaquitaController> routes=new HashMap<String, VaquitaController>();

        routes.put("/", new IndexController());

        routes.put("/dashboard",new DashboardController());
        routes.put("/leads",new LeadsController());



        routes.put("/sales",new SalesController());
        routes.put("/sales/edit",new SalesEditController());



        routes.put("/expenses",new ExpensesController());
        routes.put("/expenses/edit",new ExpensesEditController());

        routes.put("/projects",new ProjectsController());
        routes.put("/projects/add",new ProjectAddController());
        routes.put("/projects/edit",new ProjectEditController());
        routes.put("/projects/view",new ProjectViewController());

        routes.put("/tasks/add",new TaskAddController());
        routes.put("/tasks/action",new TaskActionController());

        routes.put("/invoices",new InvoicesController());

        routes.put("/profile",new ProfileController());

        routes.put("/login",new LoginController());
        routes.put("/logout",new LogoutController());

        routes.put("/register",new RegisterController());



        routes.put("/api/cashflow",new CashFlowEndpoint());
        routes.put("/api/businessvaluehistory",new BusinessValueHistoryEndpoint());

        routes.put("/api/value",new BalanceEndpoint());
        routes.put("/api/salesthismonth",new SalesThisMonthEndpoint());
        routes.put("/api/expensesthismonth",new ExpensesThisMonthEndpoint());
        routes.put("/api/profit",new ProfitEndpoint());
        routes.put("/api/activeprojects",new ActiveProjectsEndpoint());

        try {
            VaquitaApp app = new VaquitaApp(9377, routes);
            app.startServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
