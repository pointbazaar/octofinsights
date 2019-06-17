package org.vanautrui.octofinsights.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

import static j2html.TagCreator.*;
import static j2html.TagCreator.script;
import static org.vanautrui.octofinsights.generated.tables.Leads.LEADS;

public class LeadsController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest request) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ){

            Connection conn= DBUtils.makeDBConnection();
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node =  mapper.createArrayNode();

            //Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortAsc(LEADS.LEAD_STATUS.startsWith("open"));
            Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortDesc(LEADS.LEAD_STATUS, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if(o1.startsWith("open")){
                        return 1;
                    }
                    if(o2.startsWith("open")){
                        return -1;
                    }
                    return 0;
                }
            });

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(),
                                    div(
                                            div(attrs("#main-content"),
                                                    h1("Leads"),
                                                    form(
                                                            input().withName("name").withPlaceholder("name"),
                                                            input().withName("what_the_lead_wants").withPlaceholder("what the lead wants"),
                                                            button(attrs(".btn .btn-outline-success"),"Insert").withType("submit")
                                                    ).withAction("/leads?action=insert").withMethod("post"),
                                                    table(
                                                            attrs(".table"),
                                                            thead(
                                                                    th("ID").attr("scope","col"),
                                                                    th("Lead Name").attr("scope","col"),
                                                                    th("Lead Status").attr("scope","col"),
                                                                    th("What the Lead wants").attr("scope","col"),
                                                                    th("Actions").attr("scope","col")
                                                            ),
                                                            tbody(
                                                                    each(
                                                                            records,
                                                                            record ->
                                                                                    tr(
                                                                                            td(record.get(LEADS.ID).toString()),
                                                                                            td(record.get(LEADS.LEAD_NAME)),
                                                                                            td(record.get(LEADS.LEAD_STATUS)),
                                                                                            td(record.get(LEADS.WHAT_THE_LEAD_WANTS)),
                                                                                            td(
                                                                                                div(attrs(".row"),
                                                                                                        form(
                                                                                                                input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                button(attrs(".btn .btn-outline-danger"),"delete").withType("submit")
                                                                                                        ).withAction("/leads?action=delete").withMethod("post"),

                                                                                                        /*Open the lead again, after it has been closed.
                                                                                                         * Some people become repeat customers.
                                                                                                         * Some people get back to you, even after you have forgotten them
                                                                                                         * */
                                                                                                        iff(
                                                                                                                record.get(LEADS.LEAD_STATUS).startsWith("closed"),
                                                                                                                form(
                                                                                                                        input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                        button(attrs(".btn .btn-outline-danger"),"open").withType("submit")
                                                                                                                ).withAction("/leads?action=open").withMethod("post")
                                                                                                        ),


                                                                                                        /*close the lead. either they accepted to become a client, or not,
                                                                                                         * the important thing is that we no longer worry about the lead
                                                                                                         * */
                                                                                                        iff(
                                                                                                            record.get(LEADS.LEAD_STATUS).startsWith("open"),
                                                                                                            form(
                                                                                                                    input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                    button(attrs(".btn .btn-outline-danger"),"close").withType("submit")
                                                                                                            ).withAction("/leads?action=close").withMethod("post")
                                                                                                        )
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
            return new VaquitaRedirectResponse("/login", request);
        }
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {

        VaquitaHTTPRequest request = vaquitaHTTPEntityEnclosingRequest;
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ) {

            String action = vaquitaHTTPEntityEnclosingRequest.getQueryParameter("action");

            if(action.equals("delete") && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")){

                System.out.println("step 2");
                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                //delete the lead with that id
                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.deleteFrom(LEADS).where( (LEADS.ID).eq(id) ).execute();

                conn.close();
            }

            if(action.equals("insert")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("name")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("what_the_lead_wants")
            ){

                String name = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("name");
                String what_the_lead_wants = vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("what_the_lead_wants");

                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.insertInto(LEADS).columns(LEADS.LEAD_NAME,LEADS.LEAD_STATUS,LEADS.DATE_OF_LEAD_ENTRY, LEADS.WHAT_THE_LEAD_WANTS).values(name,"open_contacted",new Timestamp(new Date().getTime()),what_the_lead_wants).execute();

                conn.close();
            }

            if(
                    (action.equals("open") || action.equals("close")) && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")
            ){

                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                Connection conn= DBUtils.makeDBConnection();

                String new_status = (action.equals("open"))?"open_contacted":"closed_not_converted";

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.update(LEADS).set(LEADS.LEAD_STATUS,new_status).where(LEADS.ID.eq(id)).execute();

                conn.close();
            }

            return new VaquitaHTMLResponse(200,"<html><a href='/leads'>go back to leads</a></html>");
        }else {
            return new VaquitaHTMLResponse(400,"<html><a href='/login'>go back to login. this was unauthenticated request</a></html>");
        }
    }
}
