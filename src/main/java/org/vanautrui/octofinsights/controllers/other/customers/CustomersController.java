package org.vanautrui.octofinsights.controllers.other.customers;

import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.IVaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;

import java.util.ArrayList;

import static j2html.TagCreator.*;

public class CustomersController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            //https://www.youtube.com/watch?v=JRWox-i6aAk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=2

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            ArrayList list = new ArrayList();
            list.add(new Object());

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Customers"),
                                    div(attrs(".container"),
                                            div(attrs("#main-content"),
                                                    form(
                                                            input().withName("search").withPlaceholder("search").withType("text"),
                                                            button(attrs(".btn .btn-outline-info"),"Search").withType("submit")
                                                    ).withAction("/customers").withMethod("get"),
                                                    h3("Add a Customer"),
                                                    form(
                                                            input().withType("text").withName("customer-name").withPlaceholder("name"),
                                                            input().withType("text").withName("customer-source").withPlaceholder("source"),
                                                            button(attrs(".btn .btn-outline-success"),"Insert").withType("submit")
                                                    ).withAction("/leads?action=insert").withMethod("post"),
                                                    table(
                                                            attrs(".table"),
                                                            thead(
                                                                    //th("ID").attr("scope","col"),
                                                                    th("Customer Name").attr("scope","col"),
                                                                    th("has active Project?").attr("scope","col"),
                                                                    th("Source").attr("scope","col")
                                                            ),
                                                            tbody(
                                                                    each(
                                                                            list,
                                                                            record ->
                                                                                    tr(
                                                                                            td(),
                                                                                            td(),
                                                                                            td(),
                                                                                            td()
                                                                                    )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )

                            )
                    ).render();

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

            return new VaquitaRedirectToGETResponse("/customers",request);
        }else {
            return new VaquitaRedirectToGETResponse("/login",request);
        }
    }
}
