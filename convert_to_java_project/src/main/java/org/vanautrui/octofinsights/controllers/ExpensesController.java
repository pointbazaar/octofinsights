package org.vanautrui.octofinsights.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.generated.tables.Expenses;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Expenses.EXPENSES;

public class ExpensesController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest request) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            Connection conn= DBUtils.makeDBConnection();
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node =  mapper.createArrayNode();
            
            Result<Record> records = create.select(EXPENSES.asterisk()).from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id)).fetch().sortDesc(EXPENSES.EXPENSE_DATE);

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username")),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    h1("EXPENSES"),
                                                    form(
                                                            input().withName("expense_name").withPlaceholder("expense_name").withType("text"),
                                                            input().withName("expense_date").withPlaceholder("expense_date").withType("date"),
                                                            input().withName("expense_value").withPlaceholder("expense_value").withType("number"),
                                                            button(attrs(".btn .btn-outline-success"),"Insert").withType("submit")
                                                    ).withAction("/expenses?action=insert").withMethod("post"),
                                                    table(
                                                            attrs(".table"),
                                                            thead(
                                                                    th("ID").attr("scope","col"),
                                                                    th("Expense Name").attr("scope","col"),
                                                                    th("Expense Date").attr("scope","col"),
                                                                    th("Expense Value").attr("scope","col"),
                                                                    th("Actions").attr("scope","col")
                                                            ),
                                                            tbody(
                                                                    each(
                                                                            records,
                                                                            record ->
                                                                                    tr(
                                                                                            td(record.get(EXPENSES.ID).toString()),
                                                                                            td(record.get(EXPENSES.EXPENSE_NAME)),
                                                                                            td(record.get(EXPENSES.EXPENSE_DATE).toString()),
                                                                                            td(record.get(EXPENSES.EXPENSE_VALUE).toString()),
                                                                                            td(
                                                                                                div(attrs(".row"),
                                                                                                        form(
                                                                                                                input().withName("id").isHidden().withValue(record.get(EXPENSES.ID).toString()),
                                                                                                                button(attrs(".btn .btn-outline-danger"),"delete").withType("submit")
                                                                                                        ).withAction("/expenses?action=delete").withMethod("post")
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

            conn.close();
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

            String action = vaquitaHTTPEntityEnclosingRequest.getQueryParameter("action");

            if(action.equals("delete") && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")){

                System.out.println("step 2");
                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                //delete the lead with that id
                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.deleteFrom(EXPENSES).where( (EXPENSES.ID).eq(id).and((EXPENSES.USER_ID).eq(user_id)) ).execute();

                conn.close();
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

                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create
                        .insertInto(EXPENSES)
                        .columns(EXPENSES.EXPENSE_NAME,EXPENSES.EXPENSE_DATE,EXPENSES.EXPENSE_VALUE, EXPENSES.USER_ID)
                        .values(expense_name,expense_date_timestamp,expense_value,user_id).execute();

                conn.close();
            }

            return new VaquitaRedirectToGETResponse("/expenses",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
