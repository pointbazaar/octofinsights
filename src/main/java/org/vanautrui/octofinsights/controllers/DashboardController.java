package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.ExpensesService;
import org.vanautrui.octofinsights.services.LeadsService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.*;

import static j2html.TagCreator.*;


public class DashboardController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest request) throws Exception {



        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ){

            int user_id = Integer.parseInt(request.session().get().get("user_id"));

            long balance = (SalesService.getTotal(user_id)+ ExpensesService.getTotal(user_id));

            long lead_count = LeadsService.getOpenLeadsCount(user_id);

            String classes_balance = (balance>=0)?"text-success":"text-danger";

            String classes_leads = (lead_count>0)?"text-success":"text-info";

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

                            div(
                                    makeDashboardCard("Balance",balance+" €","",classes_balance),
                                    makeDashboardCard("Open Leads",""+ lead_count,"",classes_leads),
                                    makeDashboardCard("Sales this Month",SalesService.getTotalForThisMonth(user_id)+" €","","")
                                    //makeDashboardCard("TODO: Business Health","Good","",""),
                                    //makeDashboardCard("TODO: lifeline","4 Weeks","","")
                            ).withClasses("row align-items-center justify-content-center")
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

    private static ContainerTag makeDashboardCard(String text,String text2,String classes1, String classes2){
        return

        div(
                div(
                        text
                ).withClasses(" text-center mt-1 "+classes1),
                div(
                        text2
                ).withClasses("text-center m-3 "+classes2).withStyle("font-size: 1.8em;")

        ).withClasses("card shadow  p-1 m-3")
        .withStyle("height: 10rem; width: 13rem;");
    }



    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        return new VaquitaTextResponse(404,"not implemented");
    }
}


