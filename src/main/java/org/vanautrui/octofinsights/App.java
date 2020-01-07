package org.vanautrui.octofinsights;

import org.vanautrui.octofinsights.controllers.*;
import org.vanautrui.octofinsights.controllers.api.*;
import org.vanautrui.octofinsights.controllers.auth.*;
import org.vanautrui.octofinsights.controllers.other.customers.*;
import org.vanautrui.octofinsights.controllers.other.expenses.*;
import org.vanautrui.octofinsights.controllers.other.invoices.*;
import org.vanautrui.octofinsights.controllers.other.leads.*;
import org.vanautrui.octofinsights.controllers.other.projects.*;
import org.vanautrui.octofinsights.controllers.other.sales.*;
import org.vanautrui.octofinsights.controllers.other.tasks.*;

import static spark.Spark.*;


public final class App
{
    /*
    Good Music:
    https://www.youtube.com/watch?v=x2P7nDtXg-A
     */

    public static final String octofinsights_primary_color = "#ffc143";

    public static void main( String[] args )
    {
        System.out.println( "Octofinsights starting ... " );
        //https://www.youtube.com/watch?v=v1olRFug2ZM

        port(9377);
        staticFiles.location("/");
        final int cache_time_seconds=30;

        staticFiles.expireTime(cache_time_seconds);


        get("/",IndexController::get);

        get("/dashboard",DashboardController::get);

        // /leads
        get("/leads",LeadsController::get);
        post("/leads",LeadsController::post);

        // /sales

        get("/sales",SalesController::get);
        post("/sales",SalesController::post);

        path("/sales",()->{
            get("/edit",SalesEditController::get);
            post("/edit",SalesEditController::post);
        });

        // /expenses

        get("/expenses",ExpensesController::get);
        post("/expenses",ExpensesController::post);

        path("/expenses",()->{
            get("/edit",ExpensesEditController::get);
            post("/edit",ExpensesEditController::post);
        });

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


        get("/customers",CustomersController::get);
        post("/customers",CustomersController::post);
        get("/customers/view",CustomerViewController::get);

        // /tasks
        path("/tasks",()->{
            post("/add",TaskAddController::post);
            post("/action",TaskActionController::post);
        });

        get("/invoices",InvoicesController::get);
        post("/invoices",InvoicesController::post);

        get("/profile",ProfileController::get);

        // /login
        get("/login",LoginController::get);
        post("/login",LoginController::post);

        get("/logout",LogoutController::get);

        get("/register",RegisterController::get);
        post("/register",RegisterController::post);

        // /api

        path("/api",()->{
            get("/cashflow",CashFlowEndpoint::get);
            get("/businessvaluehistory",BusinessValueHistoryEndpoint::get);
            get("/mostprofitablecustomers",MostProfitableCustomersEndpoint::get);

            get("/current_balance",BalanceEndpoint::get);
            get("/salesthismonth",SalesThisMonthEndpoint::get);
            get("/expensesthismonth",ExpensesThisMonthEndpoint::get);
            get("/profit",ProfitEndpoint::get);
            get("/activeprojects",ActiveProjectsEndpoint::get);
            get("/activetasks",ActiveTasksEndpoint::get);
            get("/openleads",OpenLeadsEndpoint::get);
            get("/projects_gantt",ProjectsGanttEndpoint::get);
        });

    }
}
