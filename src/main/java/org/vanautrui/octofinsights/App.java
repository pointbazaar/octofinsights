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
import spark.Spark;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static spark.Spark.*;
import static spark.route.HttpMethod.post;


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

        // /projects
        get("/projects",ProjectsController::get);
        post("/projects",ProjectsController::post);
        path("/projects",()->{
            get("/view",ProjectViewController::get);

            get("/add",ProjectAddController::get);
            post("/add",ProjectAddController::post);

            get("/edit",ProjectEditController::get);
            post("/edit",ProjectEditController::post);
        });



        app.putFullMapping("/customers",new CustomersController());
        app.putGetMapping("/customers/view",new CustomerViewController());

        // /tasks

        app.putPostMapping("/tasks/add",new TaskAddController());
        app.putPostMapping("/tasks/action",new TaskActionController());

        app.putGetMapping("/invoices",new InvoicesController());

        app.putGetMapping("/profile",new ProfileController());

        app.putFullMapping("/login",new LoginController());
        app.putGetMapping("/logout",new LogoutController());

        app.putFullMapping("/register",new RegisterController());

        // /api

        path("/api",()->{
            get("/cashflow",CashFlowEndpoint::get);
            get("/businessvaluehistory",BusinessValueHistoryEndpoint::get);
            get("current_balance",BalanceEndpoint::get);
            get("/salesthismonth",SalesThisMonthEndpoint::get);
            get("/expensesthismonth",ExpensesThisMonthEndpoint::get);
            get("/profit",ProfitEndpoint::get);
            get("/activeprojects",ActiveProjectsEndpoint::get);
            get("/activetasks",ActiveTasksEndpoint::get);
            get("/openleads",OpenLeadsEndpoint::get);
        });


        try {
            app.startServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
