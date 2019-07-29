package org.vanautrui.octofinsights.controllers.other.leads;

import j2html.tags.ContainerTag;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.LeadsService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Leads.LEADS;

public class LeadsController extends VaquitaController {

    private static ContainerTag makeLeadBadge(String lead_status){

        String status_sensitive="";

        switch (lead_status){
            case "converted":
                status_sensitive="badge-light";
                break;
            case "open":
                status_sensitive="badge-primary";
                break;
            case "closed":
                status_sensitive="badge-secondary";
                break;
        }

        return
        div(
                lead_status
        ).withClasses("badge "+status_sensitive);
    }

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            Optional<String> searchQuery;
            try{
                searchQuery=Optional.of(request.getQueryParameter("search"));
            }catch (Exception e){
                searchQuery=Optional.empty();
            }


            List<Record> filtered_records = LeadsService.getLeads(user_id,searchQuery);



            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Leads"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    form(
                                                            input().withName("search").withPlaceholder("search").withType("text"),
                                                            button(attrs(".btn .btn-outline-info"),"Search").withType("submit")
                                                    ).withAction("/leads").withMethod("get"),
                                                    form(
                                                            input().withName("name").withPlaceholder("name"),
                                                            input().withName("what_the_lead_wants").withPlaceholder("what the lead wants"),
                                                            button(attrs(".btn .btn-outline-success"),"Insert").withType("submit")
                                                    ).withAction("/leads?action=insert").withMethod("post"),
                                                    table(
                                                            attrs(".table"),
                                                            thead(
                                                                    //th("ID").attr("scope","col"),
                                                                    th("Lead Name").attr("scope","col"),
                                                                    th("Lead Status").attr("scope","col"),
                                                                    th("What the Lead wants").attr("scope","col"),
                                                                    th("Actions").attr("scope","col")
                                                            ),
                                                            tbody(
                                                                    each(
                                                                            filtered_records,
                                                                            record ->
                                                                                    tr(
                                                                                            //td(record.get(LEADS.ID).toString()),
                                                                                            td(record.get(LEADS.LEAD_NAME)),
                                                                                            td(makeLeadBadge(record.get(LEADS.LEAD_STATUS))),
                                                                                            td(record.get(LEADS.WHAT_THE_LEAD_WANTS)),
                                                                                            td(
                                                                                                div(attrs(".row .align-items-center"),
                                                                                                        form(
                                                                                                                input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                RecordEditIconUtils.deleteButton()
                                                                                                        ).withAction("/leads?action=delete").withMethod("post"),

                                                                                                        /*Open the lead again, after it has been closed.
                                                                                                         * Some people become repeat customers.
                                                                                                         * Some people get back to you, even after you have forgotten them
                                                                                                         * */
                                                                                                        iff(
                                                                                                                record.get(LEADS.LEAD_STATUS).startsWith("closed"),
                                                                                                                form(
                                                                                                                        input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                        button(attrs(".btn .btn-outline-info .p-2 .m-1"),"open").withType("submit")
                                                                                                                ).withAction("/leads?action=open").withMethod("post")
                                                                                                        ),

                                                                                                        iff(
                                                                                                                record.get(LEADS.LEAD_STATUS).startsWith("open"),
                                                                                                                form(
                                                                                                                        input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                        button(attrs(".btn .btn-outline-info .p-2 .m-1"),"convert").withType("submit")
                                                                                                                ).withAction("/leads?action=convert").withMethod("post")
                                                                                                        ),

                                                                                                        /*close the lead. either they accepted to become a client, or not,
                                                                                                         * the important thing is that we no longer worry about the lead
                                                                                                         * */
                                                                                                        iff(
                                                                                                            record.get(LEADS.LEAD_STATUS).startsWith("open"),
                                                                                                            form(
                                                                                                                    input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                                                    button(attrs(".btn .btn-outline-info .p-2 .m-1"),"close").withType("submit")
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

            //conn.close();
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
        ){

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            String action = vaquitaHTTPEntityEnclosingRequest.getQueryParameter("action");

            if(action.equals("delete") && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")){

                System.out.println("step 2");
                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                //delete the lead with that id
                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.deleteFrom(LEADS).where( (LEADS.ID).eq(id).and(LEADS.USER_ID.eq(user_id)) ).execute();

                conn.close();
            }

            if(action.equals("insert")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("name")
                    && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("what_the_lead_wants")
            ){

                Map<String,String> post_parameters =vaquitaHTTPEntityEnclosingRequest.getPostParameters();

                String name = URLDecoder.decode(post_parameters.get("name"));
                String what_the_lead_wants = URLDecoder.decode(post_parameters.get("what_the_lead_wants"));

                Connection conn= DBUtils.makeDBConnection();

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.insertInto(LEADS).columns(LEADS.LEAD_NAME, LEADS.LEAD_STATUS, LEADS.DATE_OF_LEAD_ENTRY, LEADS.WHAT_THE_LEAD_WANTS,LEADS.USER_ID)
                        .values(name,"open",new Timestamp(new Date().getTime()),what_the_lead_wants,user_id).execute();

                conn.close();
            }

            if(
                    (action.equals("open") || action.equals("close") || action.equals("convert")) && vaquitaHTTPEntityEnclosingRequest.getPostParameters().containsKey("id")
            ){

                int id = Integer.parseInt(vaquitaHTTPEntityEnclosingRequest.getPostParameters().get("id"));

                Connection conn= DBUtils.makeDBConnection();

                String new_status = (action.equals("open"))?"open":"closed";

                if(action.equals("convert")){
                    new_status="converted";
                }

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.update(LEADS).set(LEADS.LEAD_STATUS,new_status).where(LEADS.ID.eq(id).and(LEADS.USER_ID.eq(user_id))).execute();

                conn.close();
            }

            return new VaquitaRedirectToGETResponse("/leads",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
