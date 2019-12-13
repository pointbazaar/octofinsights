package org.vanautrui.octofinsights.controllers.other.customers;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import spark.Request;
import spark.Response;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public final class CustomersController {

    public static Object get(Request req, Response res) {
        if(  req.session().attributes().contains("authenticated")
                && req.session().attributes().contains("user_id")
        ){
            //https://www.youtube.com/watch?v=JRWox-i6aAk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=2

            int user_id = Integer.parseInt(req.session().attribute("user_id"));

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
                                    NavigationUtil.createNavbar(req.session().attribute("username"),"Customers"),
                                    div(
                                            div(
                                                    CustomersJ2HTMLUtils.makeCustomerInsertWidget(),
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
        if( req.session().attributes().contains("authenticated") && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){

            int user_id = Integer.parseInt(req.session().attribute("user_id"));
            final String action = req.queryParams("action");

            final String customer_name=req.queryParams("customer-name");
            final String customer_source=req.queryParams("customer-source");

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
