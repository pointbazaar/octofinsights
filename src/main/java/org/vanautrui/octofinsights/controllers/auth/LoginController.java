package org.vanautrui.octofinsights.controllers.auth;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import spark.Request;
import spark.Response;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;
import static org.vanautrui.octofinsights.html_util_domain_specific.ErrorUtil.errorHTML;

public final class LoginController {

    public static Object get(Request req, Response res) {
        final ContainerTag page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                div(
                                        attrs(".container"),
                                        div(
                                                div(
                                                        h1("Octofinsights Login").withClasses("text-center"),
                                                        form(
                                                                div(
                                                                        label("Username"),
                                                                        input().withName("username").withType("text").withClasses("form-control")
                                                                ).withClasses("form-group"),
                                                                div(
                                                                        label("Password"),
                                                                        input().withName("password").withPlaceholder("password").withType("password").withClasses("form-control")
                                                                ).withClasses("form-group"),
                                                                button(attrs(".btn .btn-primary .col-md-12"),"Login").withType("submit")
                                                        ).withAction("/login").withMethod("post")
                                                )
                                        ).withClasses("row justify-content-center")
                                )
                        )
                );

        res.status(200);
        res.type(ContentType.TEXT_HTML.toString());
        return page.render();
    }

    public static Object post(Request req, Response res) {
        //verify login credentials and set cookie

        final String username = URLDecoder.decode(req.queryParams("username"));
        final String password = URLDecoder.decode(req.queryParams("password"));

        System.out.println(username);
        System.out.println(password);

        final Connection conn;
        try {
            conn = DBUtils.makeDBConnection();
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return e.getMessage();
        }

        final DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        final Result<Record2<Integer, String>> does_it_login= create
                .select(USERS.ID,USERS.PASSWORD)
                .from(USERS)
                .where(
                        USERS.USERNAME.eq(username)
                )
                .fetch();
        Optional<Integer> id = Optional.empty();
        String password_in_db = "";

        if(does_it_login.size()>0){
            for(Record r : does_it_login){
                id=Optional.of(r.getValue(USERS.ID));
                password_in_db=r.get(USERS.PASSWORD);
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }


        if(id.isPresent() && password.equals(password_in_db)) {
            req.session(true);
            req.session().attribute("authenticated", "true");
            req.session().attribute("username", req.queryParams("username"));
            req.session().attribute("user_id", id.get().toString());

            res.redirect("/");
            return "";
        }else {
            res.status(400);
            res.type(ContentType.TEXT_HTML.toString());

            if(id.isPresent()){
                return errorHTML("user exists, but password was wrong.").renderFormatted();
            }else {
                return errorHTML("user "+username+" does not exist.").renderFormatted();
            }
        }

    }
}
