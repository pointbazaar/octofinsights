package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;


public class IndexController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest request) throws Exception {

        String[] sidebar_links = new String[]{"/sales","/leads","/expenses","/employees","/inventory"};

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ){

            ContainerTag sidebar = div(
                    attrs("#sidebar .col-md-2 .sidebar .p-3 .bg-light"),
                    a(h2("Octofinsights")).withHref("/"),
                    div(
                        each(
                                Arrays.stream(sidebar_links).collect(Collectors.toList()),
                                link->div(
                                        attrs(".m-3"),
                                        hr(),
                                        a(strong(link.substring(1).toUpperCase()))
                                                .withHref(link)
                                )
                        )
                    )
            );

            String page=
            html(
                head(
                    title("Octofinsights"),
                    link().withRel("stylesheet").withHref("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
                ),
                body(
                    div(
                        attrs(".row"),
                        sidebar,
                        div(attrs("#main-content"),
                            h1("Hello"),
                            p(request.session().get().get("username")),
                            p("you have logged in correctly"),
                            img().withSrc("/img/hello.png"),
                            canvas(attrs("#myChart"))
                        )
                    ),
                    script().withSrc("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.js"),
                    script().withSrc("/cashflow_chart.js")

                )
            ).render();

            return new VaquitaHTMLResponse(200,page);

        }else {
            return new VaquitaRedirectResponse("/login.html", request);
        }
    }



    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        return new VaquitaTextResponse(404,"not implemented");
    }
}


