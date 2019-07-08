package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.sql.Connection;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public class RegisterController extends VaquitaController {



    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {

        ContainerTag page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                div(
                                        attrs(".container"),
                                        h1("Register"),
                                        form(
                                                label("username"),
                                                input().withName("username").withPlaceholder("username").withValue("test").withType("text"),
                                                label("Password"),
                                                input().withName("password").withPlaceholder("password").withValue("test").withType("password")/*,
                                                label("Business"),
                                                input().withName("business").withPlaceholder("business").withValue("test").withType("text")*/,
                                                button(attrs(".btn .btn-primary"),"Login").withType("submit")
                                        ).withAction("/register").withMethod("post"),
                                        p(
                                                "test credentials: 'test','test','vanautrui'"
                                        )
                                )
                        )
                );

        return new VaquitaHTMLResponse(200,page.render());
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {

        Map<String,String> params= vaquitaHTTPEntityEnclosingRequest.getPostParameters();

        String username = params.get("username");
        String password = params.get("password");

        //insert new user into the database

        Connection conn = DBUtils.makeDBConnection();

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Result<Record1<Integer>> fetch = create.select(USERS.ID).from(USERS).where(USERS.USERNAME.eq(username)).fetch();

        if(fetch.size()==0) {
            create.insertInto(USERS).columns(USERS.USERNAME, USERS.PASSWORD).values(username, password).execute();
        }else{
            System.out.println("\t user with this username already exists");
        }

        conn.close();

        return new VaquitaRedirectToGETResponse("/",vaquitaHTTPEntityEnclosingRequest);
    }
}
