package org.vanautrui.octofinsights.controllers.other.customers;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.jooq.Result;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import spark.Request;
import spark.Response;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeSalesTable;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public final class CustomerViewController {


    public static Object get(Request request, Response response) {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            //https://www.youtube.com/watch?v=JRWox-i6aAk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=2

            int user_id = parseInt(request.session().get().get("user_id"));
            int customer_id = parseInt(request.getQueryParam("id"));
            Record customer = null;
            try {
                customer = CustomersService.getCustomerById(user_id,customer_id);
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                response.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            //TODO: use this to make a table, reuse the code from the sales view
            Result<Record> sales_to_this_customer = null;
            try {
                sales_to_this_customer = SalesService.getSalesToCustomerByCustomerId(user_id,customer_id);
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                response.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            String customer_name = customer.get(CUSTOMERS.CUSTOMER_NAME);

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Customers"),
                                    div(
                                            div(
                                                    h3("Customer: "+customer_name),
                                                    p("Source: "+customer.get(CUSTOMERS.SOURCE)),
                                                    p("Acquisition Date: "+customer.get(CUSTOMERS.ACQUISITION_DATE)),
                                                    p("TODO: display info about the customer, the sales related to him, and the projects with him"),
                                                    h5("Sales to "+customer_name+":"),
                                                    makeSalesTable(user_id,sales_to_this_customer)
                                            ).withId("main-content")
                                    ).withClasses("container")
                            )
                    ).render();

            response.status(200);
            response.type(ContentType.TEXT_HTML.toString());
            return page;

        }else {
            response.redirect("/login");
            return "";
        }
    }
}
