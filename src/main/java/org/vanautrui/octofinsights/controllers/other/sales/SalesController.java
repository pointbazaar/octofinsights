package org.vanautrui.octofinsights.controllers.other.sales;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.octofinsights.services.SalesService;
import spark.Request;
import spark.Response;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeSalesInsertWidget;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeSalesTable;

public final class SalesController {

    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){
            final int user_id = parseInt(req.session().attribute("user_id"));

            final List<Record> records;
            try {
                records = SalesService.getSales(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            final ContainerTag mytable = makeSalesTable(user_id,records);

            final List<Record> all_customers;
            try {
                all_customers = CustomersService.getCustomers(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                res.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            if(all_customers.size()==0){

                res.status(400);
                res.type(ContentType.TEXT_PLAIN.toString());
                res.body("Please first create a Customer, to view the Sales Section ");
            }

            final String page;
            try {
                page = html(
                        HeadUtil.makeHead(),
                        body(
                                NavigationUtil.createNavbar(req.session().attribute("username"),"Sales"),
                                div(
                                        div(
                                                makeSalesInsertWidget(user_id),
                                                mytable
                                        ).withId("main-content")
                                ).withClasses("container")

                        )
                ).render();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }


            res.status(200);
            res.type(ContentType.TEXT_HTML.toString());
            return page;

        }else {
            res.redirect("/login");
            return "";
        }
    }

    public static Object post(Request req, Response res) {
        if(  req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ) {

            final int user_id = parseInt(req.session().attribute("user_id"));

            final String action = req.queryParams("action");

            if(action.equals("delete") && req.queryParams().contains("id")){

                int id = parseInt(req.queryParams("id"));
                try {
                    SalesService.deleteById(id,user_id);
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }else if(action.equals("insert")
                    && req.queryParams().contains("customer_id")
                    && req.queryParams().contains("product_or_service")
                    && req.queryParams().contains("price_of_sale")
            ){

                final int customer_id = parseInt(req.queryParams("customer_id"));
                final String product_or_service= req.queryParams("product_or_service");
                final int price= parseInt(req.queryParams("price_of_sale"));
                final String time_of_sale = req.queryParams("time_of_sale");

                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

                Timestamp date_of_sale = new Timestamp(date.getTime());

                try {
                    SalesService.insert(user_id,customer_id,product_or_service,price,date_of_sale);
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }
            res.redirect("/sales");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
