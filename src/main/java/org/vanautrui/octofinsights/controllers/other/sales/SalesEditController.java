package org.vanautrui.octofinsights.controllers.other.sales;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVFullController;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPutRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import spark.Request;
import spark.Response;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeCustomerSelect;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public final class SalesEditController {

    public static Object get(Request request, Response response) {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            final int user_id = parseInt(request.session().get().get("user_id"));

            final int sale_id = parseInt(request.getQueryParam("id"));

            final Record sale = SalesService.getById(user_id,sale_id);

            final String page=
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

            response.status(200);
            response.type(ContentType.TEXT_HTML.toString());
            return page;

        }else {
            response.redirect("/login");
            return "";
        }
    }

    public static Object post(Request request, Response response) {
        if( req.session().isPresent() && req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true")
                && req.session().get().containsKey("user_id")
        ) {

            final int user_id = parseInt(req.session().get().get("user_id"));

            final Map<String,String> params = req.getPostParameters();

            final int sale_id = parseInt(params.get("id"));

            final int customer_id = parseInt(req.getPostParameters().get("customer_id"));
            final String product_or_service= URLDecoder.decode(req.getPostParameters().get("product_or_service"));
            final int price= parseInt(req.getPostParameters().get("price_of_sale"));
            final String time_of_sale = req.getPostParameters().get("time_of_sale");

            final Timestamp expense_date_timestamp = new Timestamp((new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale)).getTime());

            SalesService.updateById(user_id,sale_id,customer_id,price,expense_date_timestamp,product_or_service);
            response.redirect("/sales");
        }else{
            response.redirect("/login");
        }
        return "";
    }
}
