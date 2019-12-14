package org.vanautrui.octofinsights.controllers.other.invoices;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils.blueButton;

public final class InvoicesJ2HTMTLUtils {

    public static ContainerTag makeInvoiceItemInsertWidget(final int user_id)throws Exception{

        ContainerTag customer_select = SalesJ2HTMLUtils.makeCustomerSelect(user_id);


        return
                div(
                        div("Add an Invoice Item").withClasses("card-header"),
                        div(
                                form(
                                        div(
                                                div(
                                                        div(
                                                                span("Service/Product Provided:").withClasses("input-group-text")
                                                        ).withClasses("input-group-prepend"),
                                                        input()
                                                                .withId("product_or_service")
                                                                .withName("product_or_service")
                                                                .withType("text")
                                                                .withClasses("form-control")
                                                ).withClasses("input-group","col-md-5"),

                                                div(
                                                        div(
                                                                span("Price: ").withClasses("input-group-text")
                                                        ).withClasses("input-group-prepend"),
                                                        input()
                                                                .withId("price")
                                                                .withName("price")
                                                                .withType("number")
                                                                .attr("min","0")
                                                                .withClasses("form-control")
                                                ).withClasses("input-group","col-md-2"),

                                                div(
                                                    customer_select
                                                ).withClasses("input-group","col-md-3"),

                                                div(
                                                        blueButton("Insert").attr("onclick","entervalue").withId("enterButton")
                                                ).withClasses("col-md-2")
                                        ).withClasses("row")

                                ).withAction("/invoices?action=insert").withMethod("post")
                        ).withClasses("card-body")
                ).withClasses("m-3","card","mb-4");
    }
}
