package org.vanautrui.octofinsights.controllers.other.expenses;

import org.jooq.Record;
import org.jooq.Result;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.ExpensesService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Expenses.EXPENSES;

public class ExpensesController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            int user_id = Integer.parseInt(request.session().get().get("user_id"));
            
            Result<Record> records = ExpensesService.getExpenses(user_id);

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Expenses"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    ExpensesJ2HTMLUtils.makeExpenseInsertWidget(user_id),
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

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            String action = vaquitaHTTPEntityEnclosingRequest.getQueryParameter("action");

            if(action.equals("delete") && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")){

                System.out.println("step 2");
                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                ExpensesService.delete(id,user_id);
            }

            if(action.equals("insert")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("expense_name")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("expense_date")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("expense_value")
            ){

                String expense_name = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_name");
                String expense_date= vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_date");

                Date expenseDate = new SimpleDateFormat("yyyy-MM-dd").parse(expense_date);

                Timestamp expense_date_timestamp = new Timestamp(expenseDate.getTime());

                int expense_value= Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("expense_value"));

                if(expense_value<=0){
                    throw new Exception("should have entered a positive expense value");
                }

                expense_value*=(-1);

                ExpensesService.insert(expense_name,expense_date_timestamp,expense_value,user_id);
            }

            return new VaquitaRedirectToGETResponse("/expenses",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
