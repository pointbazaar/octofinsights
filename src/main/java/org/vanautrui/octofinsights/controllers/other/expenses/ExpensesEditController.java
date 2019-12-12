package org.vanautrui.octofinsights.controllers.other.expenses;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ExpensesService;
import spark.Request;
import spark.Response;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;

public final class ExpensesEditController {

    public static Object get(Request req, Response res) {
        if(  req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){
            int user_id = Integer.parseInt(req.session().attribute("user_id"));

            int expense_id = Integer.parseInt(req.queryParams("id"));

            Record expense = null;
            try {
                expense = ExpensesService.getById(user_id,expense_id);
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
                                    NavigationUtil.createNavbar(req.session().attribute("username"),"Expenses"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    h1("Edit an Expense"),
                                                    form(
                                                            input().withName("id").isHidden().withValue(expense.get(EXPENSES.ID).toString()),
                                                            input()
                                                                    .withName("expense_name")
                                                                    .withPlaceholder("expense_name")
                                                                    .withType("text")
                                                                    .withValue(expense.get(EXPENSES.EXPENSE_NAME)),

                                                            input()
                                                                    .withName("expense_value")
                                                                    .withPlaceholder("expense_value")
                                                                    .withType("number")
                                                                    .withValue(((-1)*expense.get(EXPENSES.EXPENSE_VALUE)) +"")
                                                                    .attr("min","0"),


                                                            input()
                                                                    .withName("expense_date")
                                                                    .withType("date")
                                                                    .withValue(expense.get(EXPENSES.EXPENSE_DATE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE).toString()),

                                                            button(attrs(".btn .btn-outline-primary"),"Submit").withType("submit")

                                                    ).withAction("/expenses/edit").withMethod("post")
                                            )
                                    )

                            )
                    ).render();


            //conn.close();

            res.status(200);
            res.type(ContentType.TEXT_HTML.toString());
            return page;

        }else {
            res.redirect("/login");
            return "";
        }
    }

    public static Object post(Request request, Response response) {
        if( request.session().attributes().contains("authenticated")
                && request.session().attribute("authenticated").equals("true")
                && request.session().attributes().contains("user_id")
        ) {

            final int user_id = Integer.parseInt(request.session().attribute("user_id"));

            final int expense_id = Integer.parseInt(request.queryParams("id"));

            final String expense_name = URLDecoder.decode(request.queryParams("expense_name"));
            final int price= (-1)*Integer.parseInt(request.queryParams("expense_value"));
            final String time_of_sale = request.queryParams("expense_date");

            Timestamp expense_date_timestamp = null;
            try {
                expense_date_timestamp = new Timestamp((new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return e.getMessage();
            }

            try {
                ExpensesService.updateById(user_id,expense_id,expense_name,price,expense_date_timestamp);
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                response.type(ContentType.TEXT_PLAIN.toString());
                return e.getMessage();
            }

            response.redirect("/expenses");
        }else{
            response.redirect("/login");
        }
        return "";
    }
}
