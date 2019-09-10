package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.LeadsService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import static j2html.TagCreator.*;


public class DashboardController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {



        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ){

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            //long value = (SalesService.getTotal(user_id)+ ExpensesService.getTotal(user_id));

            long lead_count = LeadsService.getOpenLeadsCount(user_id);

            //String classes_balance = (value>=0)?"text-success":"text-danger";

            String classes_leads = (lead_count>0)?"text-success":"text-info";



            //long sales_this_month = SalesService.getTotalForThisMonth(user_id);
            //long expenses_this_month =ExpensesService.getTotalForThisMonth(user_id);

            //long delta_this_month = sales_this_month+expenses_this_month;

            //String classes_month = (delta_this_month>=0)?"text-success":"text-danger";

            String page=
            html(
                HeadUtil.makeHead(),
                body(
                    NavigationUtil.createNavbar(request.session().get().get("username"),"Dashboard"),
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
                                    makeDashboardSimpleCard("Open Leads",""+ lead_count, classes_leads,"","leadsdiv"),
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
                    script().withSrc("target/org/vanautrui/octofinsights/frontend/DashboardApp.js")

                )
            ).render();

            return new VaquitaHTMLResponse(200,page);

        }else {
            return new VaquitaRedirectToGETResponse("/login", request);
        }
    }

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



    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return new VaquitaTextResponse(404,"not implemented");
    }
}


