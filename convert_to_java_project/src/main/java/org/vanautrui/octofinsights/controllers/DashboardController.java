package org.vanautrui.octofinsights.controllers;

import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.*;

import static j2html.TagCreator.*;


public class DashboardController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest request) throws Exception {



        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ){



            String page=
            html(
                HeadUtil.makeHead(),
                body(
                    NavigationUtil.createNavbar(request.session().get().get("username")),
                    div(attrs(".container-fluid"),
                        div(attrs("#main-content"),
                            h1("Dashboard"),
                            p(request.session().get().get("username")),
                            p("you have logged in correctly"),
                            /*img().withSrc("/img/hello.png"),*/
                            div(
                                    div(canvas(attrs("#myChart"))).withClass("col-md-6"),
                                    div(canvas(attrs("#myChartBusinessValue"))).withClass("col-md-6")
                            ).withClasses("row justify-content-center")
                        )
                    ),
                    script().withSrc("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.js"),
                    script().withSrc("/cashflow_chart.js")

                )
            ).render();

            return new VaquitaHTMLResponse(200,page);

        }else {
            return new VaquitaRedirectToGETResponse("/login", request);
        }
    }



    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        return new VaquitaTextResponse(404,"not implemented");
    }
}


