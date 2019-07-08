package org.vanautrui.octofinsights.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.sql.Connection;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class SalesController extends VaquitaController {

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

            //Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortAsc(LEADS.LEAD_STATUS.startsWith("open"));
            Result<Record> records = create.select(SALES.asterisk()).from(SALES).where(SALES.USER_ID.eq(user_id)).fetch().sortDesc(SALES.TIME_OF_SALE);

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    h1("Sales"),
                                                    form(
                                                            input().withName("customer_name").withPlaceholder("customer_name").withType("text"),
                                                            input().withName("price_of_sale").withPlaceholder("price_of_sale").withType("number"),
                                                            input().withName("product_or_service").withPlaceholder("product_or_service").withType("text"),
                                                            button(attrs(".btn .btn-outline-success"),"Insert").withType("submit")
                                                    ).withAction("/sales?action=insert").withMethod("post"),
                                                    table(
                                                            attrs(".table"),
                                                            thead(
                                                                    th("ID").attr("scope","col"),
                                                                    th("Customer ").attr("scope","col"),
                                                                    th("Price").attr("scope","col"),
                                                                    th("Product or Service").attr("scope","col"),
                                                                    th("Actions").attr("scope","col")
                                                            ),
                                                            tbody(
                                                                    each(
                                                                            records,
                                                                            record ->
                                                                                    tr(
                                                                                            td(record.get(SALES.ID).toString()),
                                                                                            td(record.get(SALES.CUSTOMER_NAME)),
                                                                                            td(record.get(SALES.PRICE_OF_SALE).toString()),
                                                                                            td(record.get(SALES.PRODUCT_OR_SERVICE)),
                                                                                            td(
                                                                                                div(attrs(".row"),
                                                                                                        form(
                                                                                                                input().withName("id").isHidden().withValue(record.get(SALES.ID).toString()),
                                                                                                                button(attrs(".btn .btn-outline-danger"),"delete").withType("submit")
                                                                                                        ).withAction("/sales?action=delete").withMethod("post")
                                                                                                )
                                                                                            )
                                                                                    )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                                    /*,
                                    script().withSrc("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.js"),
                                    script().withSrc("/cashflow_chart.js")
                                     */

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
                create.deleteFrom(SALES).where( (SALES.ID).eq(id).and((SALES.USER_ID).eq(user_id)) ).execute();

                conn.close();
            }

            if(action.equals("insert")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("customer_name")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("product_or_service")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("price_of_sale")
            ){

                String customer_name = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("customer_name");
                String product_or_service= vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("product_or_service");
                int price= Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("price_of_sale"));

                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create
                        .insertInto(SALES)
                        .columns(SALES.CUSTOMER_NAME,SALES.PRODUCT_OR_SERVICE,SALES.PRICE_OF_SALE, SALES.USER_ID)
                        .values(customer_name,product_or_service,price,user_id).execute();

                conn.close();
            }

            return new VaquitaRedirectToGETResponse("/sales",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
