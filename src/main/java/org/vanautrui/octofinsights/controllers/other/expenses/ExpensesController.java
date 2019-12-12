package org.vanautrui.octofinsights.controllers.other.expenses;

import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.jooq.Result;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.ExpensesService;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Expenses.EXPENSES;

public final class ExpensesController {

    public static Object get(Request req, Response res) {
        if( req.session().isPresent() && req.session().get().containsKey("authenticated") && req.session().get().get("authenticated").equals("true")
                && req.session().get().containsKey("user_id")
        ){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            Result<Record> records = null;
            try {
                records = ExpensesService.getExpenses(user_id);
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
                                    NavigationUtil.createNavbar(req.session().get().get("username"),"Expenses"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    ExpensesJ2HTMLUtils.makeExpenseInsertWidget(),
                                                    table(
                                                            attrs(".table"),
                                                            thead(
                                                                    //th("ID").attr("scope","col"),
                                                                    th("Expense Name").attr("scope","col"),
                                                                    th("Expense Date").attr("scope","col"),
                                                                    th("Expense Value").attr("scope","col"),
                                                                    th("Actions").attr("scope","col")
                                                            ).withClasses("thead-light"),
                                                            tbody(
                                                                    each(
                                                                            records,
                                                                            record ->
                                                                                    tr(
                                                                                            //td(record.get(EXPENSES.ID).toString()),
                                                                                            td(record.get(EXPENSES.EXPENSE_NAME)),
                                                                                            td(record.get(EXPENSES.EXPENSE_DATE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),
                                                                                            td(record.get(EXPENSES.EXPENSE_VALUE).toString()+" €"),
                                                                                            td(
                                                                                                    div(attrs(".row"),
                                                                                                            form(
                                                                                                                    input().withName("id").isHidden().withValue(record.get(EXPENSES.ID).toString()),
                                                                                                                    RecordEditIconUtils.deleteButton()
                                                                                                            ).withAction("/expenses?action=delete").withMethod("post"),

                                                                                                            form(
                                                                                                                    input().withName("id").isHidden().withValue(record.get(EXPENSES.ID).toString()),
                                                                                                                    RecordEditIconUtils.updateButton()
                                                                                                            ).withAction("/expenses/edit").withMethod("get")
                                                                                                    )
                                                                                            )
                                                                                    )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
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
        if( vaquitaHTTPEntityEnclosingRequest.session().isPresent() && vaquitaHTTPEntityEnclosingRequest.session().get().containsKey("authenticated") && vaquitaHTTPEntityEnclosingRequest.session().get().get("authenticated").equals("true")
                && vaquitaHTTPEntityEnclosingRequest.session().get().containsKey("user_id")
        ) {

            int user_id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.session().get().get("user_id"));

            String action = req.queryParams("action");

            if(action.equals("delete") && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")){

                System.out.println("step 2");
                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                try {
                    ExpensesService.delete(id,user_id);
                } catch (Exception e) {
                    return e.getMessage();
                    e.printStackTrace();
                }
            }

            if(action.equals("insert")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("expense_name")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("expense_date")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("expense_value")
            ){

                String expense_name = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_name");
                String expense_date= vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_date");

                Date expenseDate = null;
                try {
                    expenseDate = new SimpleDateFormat("yyyy-MM-dd").parse(expense_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

                Timestamp expense_date_timestamp = new Timestamp(expenseDate.getTime());

                int expense_value= Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_value"));

                if(expense_value<=0){
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return ("should have entered a positive expense value");
                }

                expense_value*=(-1);

                try {
                    ExpensesService.insert(expense_name,expense_date_timestamp,expense_value,user_id);
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }

            res.redirect("/expenses");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
