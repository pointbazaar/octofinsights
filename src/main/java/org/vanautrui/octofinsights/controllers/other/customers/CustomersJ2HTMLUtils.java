package org.vanautrui.octofinsights.controllers.other.customers;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.vanautrui.octofinsights.services.CustomersService;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Invoices.INVOICES;

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

    public static ContainerTag createLinkToCustomer(final int user_id, final int customer_id) throws Exception {
        final Record customer = CustomersService.getCustomerById(user_id, customer_id);

        return a(
            customer.get(CUSTOMERS.CUSTOMER_NAME)
        ).withHref("/customers/view?id="+customer.get(CUSTOMERS.ID));
    }
}
