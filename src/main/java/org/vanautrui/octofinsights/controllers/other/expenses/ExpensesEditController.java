package org.vanautrui.octofinsights.controllers.other.expenses;

import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ExpensesService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class ExpensesEditController extends org.vanautrui.vaquitamvc.controller.VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest request) throws Exception {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            int expense_id = Integer.parseInt(request.getQueryParameter("id"));

            Record expense = ExpensesService.getById(user_id,expense_id);

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Expenses"),
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
            return new VaquitaHTMLResponse(200,page);

        }else {
            return new VaquitaRedirectToGETResponse("/login", request);
        }
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        VaquitaHTTPRequest request = vaquitaHTTPEntityEnclosingRequest;
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ) {

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            Map<String,String> params = vaquitaHTTPEntityEnclosingRequest.getPostParameters();

            int expense_id = Integer.parseInt(params.get("id"));

            String expense_name = URLDecoder.decode(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_name"));
            int price= (-1)*Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_value"));
            String time_of_sale = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_date");

            Timestamp expense_date_timestamp = new Timestamp((new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale)).getTime());

            ExpensesService.updateById(user_id,expense_id,expense_name,price,expense_date_timestamp);

            return new VaquitaRedirectToGETResponse("/expenses",request);
        }else{
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
