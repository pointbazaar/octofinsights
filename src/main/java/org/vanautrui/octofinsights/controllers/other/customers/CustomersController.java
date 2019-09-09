package org.vanautrui.octofinsights.controllers.other.customers;

import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public class CustomersController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            //https://www.youtube.com/watch?v=JRWox-i6aAk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=2

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            List<Record> list = CustomersService.getCustomers(user_id);


            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                              NavigationUtil.createNavbar(request.session().get().get("username"),"Customers"),
                              div(
                                  div(
                                      h3("Customers"),
                                      form(
                                          div(
                                              input().withType("text").withName("customer-name").withPlaceholder("name").withClasses("form-control")
                                          ).withClasses("form-group"),
                                          div(
                                              input().withType("text").withName("customer-source").withPlaceholder("source").withClasses("form-control")
                                          ).withClasses("form-group"),
                                          button("Insert Customer")
                                                  .withType("submit")
                                                  .withClasses("btn","btn-outline-success")
                                      ).withAction("/customers?action=insert").withMethod("post"),
                                      table(
                                        attrs(".table"),
                                        thead(
                                                //th("ID").attr("scope","col"),
                                                th("Customer Name").attr("scope","col"),
                                                //th("has active Project?").attr("scope","col"),
                                                th("Source").attr("scope","col"),
                                                th("Acquisition Date").attr("scope","col")
                                        ).withClasses("thead-light"),
                                        tbody(
                                          each(
                                            list,
                                            record ->
                                              tr(
                                                td(
                                                  a(record.get(CUSTOMERS.CUSTOMER_NAME)).withHref("/customers/view?id="+record.get(CUSTOMERS.ID))
                                                ),
                                                td(record.get(CUSTOMERS.SOURCE)),
                                                td(record.get(CUSTOMERS.ACQUISITION_DATE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE))
                                              )
                                          )
                                        )
                                      )
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

        IVaquitaHTTPRequest request = vaquitaHTTPEntityEnclosingRequest;
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){

            int user_id = Integer.parseInt(request.session().get().get("user_id"));
            String action = request.getQueryParameter("action");

          Map<String, String> params = vaquitaHTTPEntityEnclosingRequest.getPostParameters();

          String customer_name=params.get("customer-name");
          String customer_source=params.get("customer-source");

          switch (action){
            case "insert":
              CustomersService.insertCustomer(user_id,customer_name,customer_source);
              break;
            case "delete":
              throw new Exception("not yet supported");
            }

            return new VaquitaRedirectToGETResponse("/customers",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
