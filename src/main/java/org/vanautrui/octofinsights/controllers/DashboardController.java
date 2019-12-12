package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.LeadsService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.controller.IVPOSTHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;
import spark.Request;
import spark.Response;

import static j2html.TagCreator.*;


public final class DashboardController {


    private static ContainerTag makeDashboardSimpleCard(String widgetName, String placeHolder, String textClasses, String textId, String cardId){
        return
        div(
                div(
                        widgetName
                ).withClasses(" text-center mt-1 "+ ""),
                div(
                        placeHolder
                ).withClasses("text-center m-3 "+textClasses).withStyle("font-size: 1.8em;").withId(textId)

        ).withClasses("card shadow  p-1 m-3")
        .withStyle("height: 10rem; width: 13rem;")
        .withId(cardId);
    }

    public static Object get(Request req, Response res) {
        if( vhttpGetRequest.session().get().containsKey("authenticated") && vhttpGetRequest.session().get().get("authenticated").equals("true") ){

            final int user_id = Integer.parseInt(vhttpGetRequest.session().get().get("user_id"));

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(vhttpGetRequest.session().get().get("username"),"Dashboard"),
                                    div(attrs(".container-fluid"),
                                            div(attrs("#main-content"),
                                                    div(
                                                            div(canvas(attrs("#myChart"))).withClass("col-md-6"),
                                                            div(canvas(attrs("#myChartBusinessValue"))).withClass("col-md-6")
                                                    ).withClasses("row justify-content-center"),

                                                    //some tiles on the dashboard are only shown conditionally. the dashboard adapts to the current
                                                    //business situation

                                                    div(
                                                            makeDashboardSimpleCard("Balance","~", "","balance","balancediv"),
                                                            makeDashboardSimpleCard("Open Leads","~","","openleads","openleadsdiv"),
                                                            makeDashboardSimpleCard("Sales this Month","~", "","salesthismonth","salesdiv"),

                                                            makeDashboardSimpleCard("Loss this Month","~", "","expensesthismonth","expensesdiv"),
                                                            makeDashboardSimpleCard("Profit this Month","~", "","profit","profitdiv"),
                                                            makeDashboardSimpleCard("Active Projects","~", "","activeprojects","activeprojectsdiv"),
                                                            makeDashboardSimpleCard("Active Tasks","~", "","activetasks","activetasksdiv")
                                                            //makeDashboardCard("TODO: Business Health","Good","",""),
                                                            //makeDashboardCard("TODO: lifeline","4 Weeks","","")
                                                    ).withClasses("row align-items-center justify-content-center"),
                                                    div().withId("test")
                                            )
                                    ),
                                    script().withSrc("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.js"),
                                    script().withSrc("/cashflow_chart.js"),
                                    script().withSrc("/formerly_jsweet/dashboardapp.js")

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
}


