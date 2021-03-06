package org.vanautrui.octofinsights.controllers.other.leads;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.LeadsService;
import spark.Request;
import spark.Response;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Optional;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Leads.LEADS;
import static org.vanautrui.octofinsights.controllers.other.leads.LeadsJ2HTMLUtils.*;
import org.vanautrui.octofinsights.html_util_domain_specific.BootstrapTableUtil;

public final class LeadsController   {

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

    public static Object get(Request req, Response res) {
        if(  req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){

            int user_id = Integer.parseInt(req.session().attribute("user_id"));

            Optional<String> searchQuery;
            try{
                searchQuery=Optional.of(req.queryParams("search"));
            }catch (Exception e){
                searchQuery=Optional.empty();
            }


            final List<Record> filtered_records;
            try {
                filtered_records = LeadsService.getLeads(user_id,searchQuery);
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
                                    NavigationUtil.createNavbar(req.session().attribute("username"),"Leads"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),

                                                    makeLeadInsertWidget(),
                                                    BootstrapTableUtil.makeBootstrapTable(
                                                        Arrays.asList("Lead Name","Lead Status","What the Lead wants","Actions"),
                                                        filtered_records,
                                                        (record) -> tr(
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
                                                                                            RecordEditIconUtils.blueButton("open")
                                                                                    ).withAction("/leads?action=open").withMethod("post")
                                                                            ),

                                                                            iff(
                                                                                    record.get(LEADS.LEAD_STATUS).startsWith("open"),
                                                                                    form(
                                                                                            input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                            RecordEditIconUtils.blueButton("convert")
                                                                                    ).withAction("/leads?action=convert").withMethod("post")
                                                                            ),

                                                                            /*close the lead. either they accepted to become a client, or not,
                                                                             * the important thing is that we no longer worry about the lead
                                                                             * */
                                                                            iff(
                                                                                    record.get(LEADS.LEAD_STATUS).startsWith("open"),
                                                                                    form(
                                                                                            input().withName("id").isHidden().withValue(record.get(LEADS.ID).toString()),
                                                                                            RecordEditIconUtils.blueButton("close")
                                                                                    ).withAction("/leads?action=close").withMethod("post")
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
        if(  req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){

            int user_id = Integer.parseInt(req.session().attribute("user_id"));

            String action = req.queryParams("action");

            if(action.equals("delete") && req.queryParams().contains("id")){

                System.out.println("step 2");
                int id = Integer.parseInt(req.queryParams("id"));

                //delete the lead with that id
                final Connection conn;
                try {
                    conn = DBUtils.makeDBConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.deleteFrom(LEADS).where( (LEADS.ID).eq(id).and(LEADS.USER_ID.eq(user_id)) ).execute();

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }

            if(action.equals("insert")
                    && req.queryParams().contains("name")
                    && req.queryParams().contains("what_the_lead_wants")
            ){

                String name = URLDecoder.decode(req.queryParams("name"));
                String what_the_lead_wants = URLDecoder.decode(req.queryParams("what_the_lead_wants"));

                Connection conn= null;
                try {
                    conn = DBUtils.makeDBConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.insertInto(LEADS).columns(LEADS.LEAD_NAME, LEADS.LEAD_STATUS, LEADS.DATE_OF_LEAD_ENTRY, LEADS.WHAT_THE_LEAD_WANTS,LEADS.USER_ID)
                        .values(name,"open",new Timestamp(new Date().getTime()),what_the_lead_wants,user_id).execute();

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }

            if(
                    (action.equals("open") || action.equals("close") || action.equals("convert")) && req.queryParams().contains("id")
            ){

                int id = Integer.parseInt(req.queryParams("id"));

                Connection conn= null;
                try {
                    conn = DBUtils.makeDBConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }

                String new_status = (action.equals("open"))?"open":"closed";

                if(action.equals("convert")){
                    new_status="converted";
                }

                DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
                create.update(LEADS).set(LEADS.LEAD_STATUS,new_status).where(LEADS.ID.eq(id).and(LEADS.USER_ID.eq(user_id))).execute();

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }

            res.redirect("/leads");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
