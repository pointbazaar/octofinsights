package org.vanautrui.octofinsights.controllers.auth;

import j2html.tags.ContainerTag;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVFullController;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPutRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;
import spark.Request;
import spark.Response;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public final class LoginController {

    public static Response get(Request request, Response response) {
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

        return new VHTMLResponse(200,page.render());
    }

    public static Response post(Request request, Response response) {
        //verify login credentials and set cookie

        final Map<String,String> parameters= req.getPostParameters();

        final String username = URLDecoder.decode(parameters.get("username"));
        final String password = URLDecoder.decode(parameters.get("password"));

        final Connection conn = DBUtils.makeDBConnection();

        final DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        final Result<Record1<Integer>> does_it_login= create
                .select(USERS.ID)
                .from(USERS)
                .where(
                        USERS.USERNAME.eq(username)
                                .and(USERS.PASSWORD.eq(password))
                )
                .fetch();
        Optional<Integer> id = Optional.empty();

        if(does_it_login.size()>0){
            for(Record r : does_it_login){
                id=Optional.of(r.getValue(USERS.ID));
            }
        }

        conn.close();

        if(req.session().isPresent()){

            if(id.isPresent()) {

                req.session().get().put("authenticated", "true");
                req.session().get().put("username", parameters.get("username"));
                req.session().get().put("user_id", id.get().toString());

                return new VRedirectToGETResponse("/",req);
            }else {
                return new VTextResponse(500,"user seems not to exist");
            }
        }else{
            return new VTextResponse(500,"something went wrong with the sessions");
        }
    }
}
