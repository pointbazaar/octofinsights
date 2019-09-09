package org.vanautrui.octofinsights.controllers.other.sales;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class SalesController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            int user_id = parseInt(request.session().get().get("user_id"));

            List<Record> records = SalesService.getSales(user_id);

            ContainerTag mytable = table(
                    thead(
                            th("Customer ").attr("scope","col"),
                            th("Price").attr("scope","col"),
                            th("Product or Service").attr("scope","col"),
                            th("Date of Sale").attr("scope","col"),
                            th("Actions").attr("scope","col")
                    ).withClasses("thead-light"),
                    tbody(
                            each(
                                    records,
                                    record ->
                                            tr(
                                                    td(SalesService.getCustomerNameForId(user_id,record.get(SALES.CUSTOMER_ID))),
                                                    td(record.get(SALES.PRICE_OF_SALE).toString()+" €"),
                                                    td(record.get(SALES.PRODUCT_OR_SERVICE)),
                                                    td(record.get(SALES.TIME_OF_SALE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),
                                                    td(
                                                            div(attrs(".row"),
                                                                    form(
                                                                            input().withName("id").isHidden().withValue(record.get(SALES.ID).toString()),
                                                                            RecordEditIconUtils.deleteButton()
                                                                    ).withAction("/sales?action=delete").withMethod("post"),

                                                                    form(
                                                                            input().withName("id").isHidden().withValue(record.get(SALES.ID).toString()),
                                                                            RecordEditIconUtils.updateButton()
                                                                    ).withAction("/sales/edit").withMethod("get")
                                                            )
                                                    )
                                            )
                            )
                    )
            ).withClasses("table");

            List<Record> all_customers = CustomersService.getCustomers(user_id);

            if(all_customers.size()==0){
              return new VaquitaTextResponse(200,"Please first create a Customer, to view the Sales Section ");
            }

            String page=
                    html(
                        HeadUtil.makeHead(),
                        body(
                            NavigationUtil.createNavbar(request.session().get().get("username"),"Sales"),
                            div(
                                div(
                                    makeSalesInsertWidget(user_id),
                                    mytable
                                ).withId("main-content")
                            ).withClasses("container")

                        )
                    ).render();

            return new VaquitaHTMLResponse(200,page);

        }else {
            return new VaquitaRedirectToGETResponse("/login", request);
        }
    }

    public static ContainerTag makeSalesInsertWidget(int user_id){
      return
      div(
          div("Add a Sale").withClasses("card-header"),
          div(
            form(
                    div(
                        div(
                                //input().withName("customer_name").withPlaceholder("customer_name").withType("text"),
                                div(
                                  span("Customer:").withClasses("input-group-text")
                                ).withClasses("input-group-prepend"),
                                makeCustomerSelect(user_id)
                        ).withClasses("input-group","col"),
                        div(
                                input().withName("price_of_sale")
                                        .withPlaceholder("price_of_sale")
                                        .withType("number")
                                        .attr("min","0")
                                        .withClasses("form-control"),
                                div(
                                        span("$").withClasses("input-group-text")
                                ).withClasses("input-group-append")
                        ).withClasses("input-group","col")
                    ).withClasses("row","mb-3"),

                    div(
                        div(
                                div(
                                        span("Product or Service").withClasses("input-group-text")
                                ).withClasses("input-group-prepend"),
                                input().withName("product_or_service").withPlaceholder("product_or_service").withType("text").withClasses("form-control")
                        ).withClasses("input-group","col"),
                        div(
                                input().withName("time_of_sale").withPlaceholder("time of sale").withType("date").withClasses("form-control")
                        ).withClasses("input-group","col")
                    ).withClasses("row","mb-3"),

                    button("Insert")
                            .withType("submit")
                            .withClasses("btn","btn-outline-success","btn-block")


            ).withAction("/sales?action=insert").withMethod("post")
          ).withClasses("card-body")
      ).withClasses("m-3","card","mb-4");
    }

    public static ContainerTag makeCustomerSelect(int user_id){

      return
      select(
              each(
                      CustomersService.getCustomers(user_id),
                      customer->
                              option(
                                      customer.get(CUSTOMERS.CUSTOMER_NAME)
                              ).withValue(customer.get(CUSTOMERS.ID)+"")
              )
      ).withClasses("custom-select").withName("customer_id")
      ;
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest entityReq,VaquitaApp app) throws Exception {

        IVaquitaHTTPRequest request = entityReq;
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ) {

            int user_id = parseInt(request.session().get().get("user_id"));

            String action = entityReq.getQueryParameter("action");

            if(action.equals("delete") && entityReq.getPostParameters().containsKey("id")){

                int id = parseInt(entityReq.getPostParameters().get("id"));
                SalesService.deleteById(id,user_id);
            }

            if(action.equals("insert")
                    && entityReq.getPostParameters().containsKey("customer_id")
                    && entityReq.getPostParameters().containsKey("product_or_service")
                    && entityReq.getPostParameters().containsKey("price_of_sale")
            ){

                int customer_id = parseInt(entityReq.getPostParameters().get("customer_id"));
                String product_or_service= entityReq.getPostParameters().get("product_or_service");
                int price= parseInt(entityReq.getPostParameters().get("price_of_sale"));
                String time_of_sale = entityReq.getPostParameters().get("time_of_sale");

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale);

                Timestamp date_of_sale = new Timestamp(date.getTime());

                SalesService.insert(user_id,customer_id,product_or_service,price,date_of_sale);
            }

            return new VaquitaRedirectToGETResponse("/sales",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
