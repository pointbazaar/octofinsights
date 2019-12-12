package org.vanautrui.octofinsights.controllers.auth;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
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
import spark.Request;
import spark.Response;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public final class RegisterController {

    public static final String regex_alphanumeric = "^[a-zA-Z0-9]+$";

    public static Object get(Request req, Response res) {
        final ContainerTag page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                div(
                                        attrs(".container"),
                                        div(
                                                div(
                                                        h1("Octofinsights Register Form").withClasses("text-center"),
                                                        form(
                                                                div(
                                                                        label("Username"),
                                                                        input()
                                                                                .withName("username")
                                                                                .withPlaceholder("username")
                                                                                .withType("text")
                                                                                .attr("pattern",regex_alphanumeric)
                                                                                .attr("required")
                                                                                .withClasses("form-control")

                                                                ).withClasses("form-group"),
                                                                div(
                                                                        label("Email"),
                                                                        input()
                                                                                .withName("email")
                                                                                .withPlaceholder("email")
                                                                                .withType("email")
                                                                                .attr("required")
                                                                                .withClasses("form-control")

                                                                ).withClasses("form-group"),
                                                                div(
                                                                        label("Password"),
                                                                        input()
                                                                                .withName("password")
                                                                                .withPlaceholder("password")
                                                                                .withType("password")
                                                                                .attr("pattern",regex_alphanumeric)
                                                                                .attr("required")
                                                                                .withClasses("form-control")
                                                                ).withClasses("form-group"),
                                                                div(
                                                                        label("Complete the Challenge: what is (2*4) + 1  "),
                                                                        input().withName("challenge").withType("text").withClasses("form-control").attr("required")
                                                                ).withClasses("form-group"),
                                                                button(attrs(".btn .btn-primary .col-md-12"),"Register").withType("submit")
                                                        )
                                                                .withAction("/register")
                                                                .withMethod("post")
                                                                .attr("autocomplete","off")
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
        final Map<String,String> params= req.getPostParameters();

        final String username = URLDecoder.decode(params.get("username"));
        final String email = URLDecoder.decode(params.get("email"));
        final String password = URLDecoder.decode(params.get("password"));

        final Integer challenge_solution = Integer.parseInt(params.get("challenge"));

        if(challenge_solution!=9){
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return "form submission did not solve challenge. could be a bot.";
        }

        if(!username.matches(regex_alphanumeric) || !password.matches(regex_alphanumeric)){
            res.status(500);
            res.type(ContentType.TEXT_PLAIN.toString());
            return "username or password do not match requested format";
        }

        //insert new user into the database

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

        final Result<Record1<Integer>> fetch = create.select(USERS.ID).from(USERS).where(USERS.USERNAME.eq(username).or(USERS.EMAIL.eq(email))).fetch();

        if(fetch.size()==0) {
            create.insertInto(USERS).columns(USERS.USERNAME, USERS.PASSWORD, USERS.EMAIL).values(username, password, email).execute();
        }else{
            System.out.println("\t user with this username or email already exists");
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        res.redirect("/");
        return "";
    }
}
