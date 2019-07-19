package org.vanautrui.octofinsights.controllers.auth;

import j2html.tags.ContainerTag;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public class LoginController extends VaquitaController {



    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {

        ContainerTag page =
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

        return new VaquitaHTMLResponse(200,page.render());
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        //verify login credentials and set cookie

        Map<String,String> parameters= vaquitaHTTPEntityEnclosingRequest.getPostParameters();

        String username = URLDecoder.decode(parameters.get("username"));
        String password = URLDecoder.decode(parameters.get("password"));

        Connection conn = DBUtils.makeDBConnection();

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Result<Record1<Integer>> does_it_login= create
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

        if(vaquitaHTTPEntityEnclosingRequest.session().isPresent()){

            if(id.isPresent()) {

                vaquitaHTTPEntityEnclosingRequest.session().get().put("authenticated", "true");
                vaquitaHTTPEntityEnclosingRequest.session().get().put("username", parameters.get("username"));
                vaquitaHTTPEntityEnclosingRequest.session().get().put("user_id", id.get().toString());

                return new VaquitaRedirectToGETResponse("/",vaquitaHTTPEntityEnclosingRequest);
            }else {
                return new VaquitaTextResponse(500,"user seems not to exist");
            }
        }else{
            return new VaquitaTextResponse(500,"something went wrong with the sessions");
        }
    }
}
