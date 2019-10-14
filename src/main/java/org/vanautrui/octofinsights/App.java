package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.DashboardController;
import org.vanautrui.octofinsights.controllers.IndexController;
import org.vanautrui.octofinsights.controllers.other.invoices.InvoicesController;
import org.vanautrui.octofinsights.controllers.ProfileController;
import org.vanautrui.octofinsights.controllers.api.*;
import org.vanautrui.octofinsights.controllers.auth.LoginController;
import org.vanautrui.octofinsights.controllers.auth.LogoutController;
import org.vanautrui.octofinsights.controllers.auth.RegisterController;
import org.vanautrui.octofinsights.controllers.other.customers.CustomerViewController;
import org.vanautrui.octofinsights.controllers.other.customers.CustomersController;
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
import org.vanautrui.vaquitamvc.VApp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App
{

    public static final String octofinsights_primary_color = "#ffc143";

    public static String yandex_api_key;
    public static final String yandex_translate_base_url ="https://translate.yandex.net/api/v1.5/tr.json/translate";

    private static void setup_translation_configuration() throws Exception{

        List<String> lines= Files.readAllLines(Paths.get("yandex-translate-key.txt"));
        yandex_api_key=lines.get(0);
    }

    public static void main( String[] args )
    {
        System.out.println( "Octofinsights starting ... " );
        //https://www.youtube.com/watch?v=v1olRFug2ZM

        try {
            setup_translation_configuration();
        }catch (Exception e){
            e.printStackTrace();
        }

        final VApp app = new VApp(9377, "Octofinsights",true);

        app.putGetMapping("/",new IndexController());

        app.putGetMapping("/dashboard",new DashboardController());

        app.putFullMapping("/leads",new LeadsController());


        app.putFullMapping("/sales",new SalesController());
        app.putFullMapping("/sales/edit",new SalesEditController());



        app.putFullMapping("/expenses",new ExpensesController());
        app.putFullMapping("/expenses/edit",new ExpensesEditController());

        app.putFullMapping("/projects",new ProjectsController());
        app.putFullMapping("/projects/add",new ProjectAddController());
        app.putFullMapping("/projects/edit",new ProjectEditController());
        app.putGetMapping("/projects/view",new ProjectViewController());

        app.putFullMapping("/customers",new CustomersController());
        app.putGetMapping("/customers/view",new CustomerViewController());

        app.putPostMapping("/tasks/add",new TaskAddController());
        app.putPostMapping("/tasks/action",new TaskActionController());

        app.putGetMapping("/invoices",new InvoicesController());

        app.putGetMapping("/profile",new ProfileController());

        app.putFullMapping("/login",new LoginController());
        app.putGetMapping("/logout",new LogoutController());

        app.putFullMapping("/register",new RegisterController());



        app.putGetMapping("/api/cashflow",new CashFlowEndpoint());
        app.putGetMapping("/api/businessvaluehistory",new BusinessValueHistoryEndpoint());

        app.putGetMapping("/api/current_balance",new BalanceEndpoint());
        app.putGetMapping("/api/salesthismonth",new SalesThisMonthEndpoint());
        app.putGetMapping("/api/expensesthismonth",new ExpensesThisMonthEndpoint());
        app.putGetMapping("/api/profit",new ProfitEndpoint());
        app.putGetMapping("/api/activeprojects",new ActiveProjectsEndpoint());
        app.putGetMapping("/api/activetasks",new ActiveTasksEndpoint());
        app.putGetMapping("/api/openleads",new OpenLeadsEndpoint());

        try {
            app.startServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
