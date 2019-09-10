package org.vanautrui.octofinsights.controllers.other.customers;

import org.jooq.Record;
import org.jooq.Result;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeSalesTable;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public class CustomerViewController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            //https://www.youtube.com/watch?v=JRWox-i6aAk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=2

            int user_id = parseInt(request.session().get().get("user_id"));
            int customer_id = parseInt(request.getQueryParameter("id"));
            Record customer = CustomersService.getCustomerById(user_id,customer_id);

            //TODO: use this to make a table, reuse the code from the sales view
            Result<Record> sales_to_this_customer = SalesService.getSalesToCustomerByCustomerId(user_id,customer_id);

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

            return new VaquitaHTMLResponse(200,page);

        }else {
            return new VaquitaRedirectToGETResponse("/login", request);
        }
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {

        return null;
    }
}