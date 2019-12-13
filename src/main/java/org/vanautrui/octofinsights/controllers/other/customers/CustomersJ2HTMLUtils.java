package org.vanautrui.octofinsights.controllers.other.customers;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public final class CustomersJ2HTMLUtils {

    public static ContainerTag makeCustomerInsertWidget() {
        return
                div(
                        div("Add a Customer").withClasses("card-header"),
                        div(
                                form(
                                        div(

                                                div(
                                                        input().withName("customer-name")
                                                                .withPlaceholder("name")
                                                                .withType("text")
                                                                .withClasses("form-control")
                                                ).withClasses("input-group","col"),
                                                div(
                                                        input().withName("customer-source")
                                                                .withPlaceholder("source")
                                                                .withType("text")
                                                                .withClasses("form-control")
                                                ).withClasses("input-group","col"),

                                                div(
                                                        button("Insert")
                                                                .withType("submit")
                                                                .withClasses("btn","btn-outline-success","btn-block")
                                                ).withClasses("input-group","col")

                                        ).withClasses("row")


                                ).withAction("/customers?action=insert").withMethod("post")
                        ).withClasses("card-body")
                ).withClasses("m-3","card");
    }
}
