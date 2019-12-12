package org.vanautrui.octofinsights.controllers.other.customers;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public final class CustomersController {

    public static Object get(Request req, Response res) {
        if( req.session().isPresent() && req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true")
                && req.session().get().containsKey("user_id")
        ){
            //https://www.youtube.com/watch?v=JRWox-i6aAk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=2

            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            final List<Record> list;
            try {
                list = CustomersService.getCustomers(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }


            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(req.session().get().get("username"),"Customers"),
                                    div(
                                            div(
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

            res.status(200);
            res.type(ContentType.TEXT_HTML.toString());
            return page;
        }else {
            res.redirect("/login");
            return "";
        }
    }

    public static Object post(Request req, Response res) {
        if( req.session().isPresent() && req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true")
                && req.session().get().containsKey("user_id")
        ){

            int user_id = Integer.parseInt(req.session().get().get("user_id"));
            final String action = req.queryParams("action");

            Map<String, String> params = req.getPostParameters();

            String customer_name=params.get("customer-name");
            String customer_source=params.get("customer-source");

            switch (action){
                case "insert":
                    try {
                        CustomersService.insertCustomer(user_id,customer_name,customer_source);
                    } catch (Exception e) {
                        e.printStackTrace();
                        res.status(500);
                        res.type(ContentType.TEXT_PLAIN.toString());
                        return e.getMessage();
                    }
                    break;
                case "delete":
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return ("not yet supported");
            }
            res.redirect("/customers");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
