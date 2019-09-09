package org.vanautrui.octofinsights.controllers.other.sales;

import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeCustomerSelect;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class SalesEditController extends org.vanautrui.vaquitamvc.controller.VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            int user_id = parseInt(request.session().get().get("user_id"));

            int sale_id = parseInt(request.getQueryParameter("id"));

            Record sale = SalesService.getById(user_id,sale_id);

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Sales"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    h1("Edit a Sale"),
                                                    form(
                                                            input().withName("id").isHidden().withValue(sale.get(SALES.ID).toString()),
                                                            label("Customer:"),
                                                            makeCustomerSelect(user_id),

                                                            div(
                                                                input()
                                                                    .withName("price_of_sale")
                                                                    .withPlaceholder("price_of_sale")
                                                                    .withType("number")
                                                                    .withValue(sale.get(SALES.PRICE_OF_SALE) +"")
                                                                    .attr("min","0")
                                                                    .withClasses("form-control"),
                                                                div(
                                                                    span("$").withClasses("input-group-text")
                                                                ).withClasses("input-group-append")
                                                            ).withClasses("input-group"),

                                                            div(
                                                                input()
                                                                    .withName("product_or_service")
                                                                    .withPlaceholder("product_or_service")
                                                                    .withType("text")
                                                                    .withValue(sale.get(SALES.PRODUCT_OR_SERVICE))
                                                                    .withClasses("form-control")
                                                            ).withClasses("input-group"),

                                                            input()
                                                                    .withName("time_of_sale")
                                                                    .withType("date")
                                                                    .withValue(sale.get(SALES.TIME_OF_SALE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE).toString()),

                                                            button(attrs(".btn .btn-outline-primary"),"Submit").withType("submit")

                                                    ).withAction("/sales/edit").withMethod("post")
                                            )
                                    )

                            )
                    ).render();


            //conn.close();
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
        ) {

            int user_id = parseInt(request.session().get().get("user_id"));

            Map<String,String> params = vaquitaHTTPEntityEnclosingRequest.getPostParameters();

            int sale_id = parseInt(params.get("id"));

            int customer_id = parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("customer_id"));
            String product_or_service= URLDecoder.decode(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("product_or_service"));
            int price= parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("price_of_sale"));
            String time_of_sale = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("time_of_sale");

            Timestamp expense_date_timestamp = new Timestamp((new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale)).getTime());

            SalesService.updateById(user_id,sale_id,customer_id,price,expense_date_timestamp,product_or_service);

            return new VaquitaRedirectToGETResponse("/sales",request);
        }else{
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
