package org.vanautrui.octofinsights.controllers.other.leads;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public final class LeadsJ2HTMLUtils {

    public static ContainerTag makeLeadInsertWidget() {
        return
                div(
                        div("Add a Lead").withClasses("card-header"),
                        div(
                                form(
                                        div(

                                                div(
                                                        input().withName("name")
                                                                .withPlaceholder("name")
                                                                .withType("text")
                                                                .withClasses("form-control")
                                                ).withClasses("input-group","col"),
                                                div(
                                                        input().withName("what_the_lead_wants")
                                                                .withPlaceholder("what the lead wants")
                                                                .withType("text")
                                                                .withClasses("form-control")
                                                ).withClasses("input-group","col"),

                                                div(
                                                        button("Insert")
                                                                .withType("submit")
                                                                .withClasses("btn","btn-outline-success","btn-block")
                                                ).withClasses("input-group","col")

                                        ).withClasses("row")


                                ).withAction("/leads?action=insert").withMethod("post")
                        ).withClasses("card-body")
                ).withClasses("m-3","card");
    }
}
