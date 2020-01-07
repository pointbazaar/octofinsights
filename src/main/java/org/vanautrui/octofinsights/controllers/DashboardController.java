package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
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
        if( req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
        ){

            final int user_id = Integer.parseInt(req.session().attribute("user_id"));

            String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(req.session().attribute("username"),"Dashboard"),
                                    div(attrs(".container-fluid"),
                                            div(attrs("#main-content"),
                                                    div(
                                                        div(
                                                            canvas().withId("myChart")
                                                        )
                                                        .withClasses("col-md-6"),

                                                        div(
                                                            canvas().withId("myChartBusinessValue")
                                                        )
                                                        .withClasses("col-md-6")

                                                    ).withClasses("row justify-content-center"),
                                                    div(
                                                        div(
                                                            canvas().withId("myChartProjects")
                                                        )
                                                        .withClasses("col-md-6","row","align-content-center"),

                                                        div(
                                                            canvas().withId("myChartCustomerProfits")
                                                        )
                                                        .withClasses("col-md-6","row","align-content-center")
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

                                    script().withSrc("/dashboard/cashflow_chart.js"), //business value history and 'sales and expenses' chart
                                    script().withSrc("/dashboard/dashboardapp.js"), //mini widgets
                                    script().withSrc("/dashboard/projects_chart.js"), //projects gantt
                                    script().withSrc("/dashboard/mostprofitablecustomers.js") //most profitable customers
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


