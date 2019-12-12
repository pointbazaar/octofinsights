package org.vanautrui.octofinsights.controllers.other.sales;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.CustomersService;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class SalesJ2HTMLUtils {

  public static ContainerTag makeSalesTable(int user_id, List<Record> records){
    ContainerTag mytable = table(
            thead(
                    th("Customer ").attr("scope","col"),
                    th("Price").attr("scope","col"),
                    th("Product or Service").attr("scope","col"),
                    th("Date of Sale").attr("scope","col"),
                    th("Actions").attr("scope","col")
            ).withClasses("thead-light"),
            tbody(
                    each(
                            records,
                            record ->
                                    tr(
                                            td(
                                                    a(
                                                            CustomersService.getCustomerNameForId(user_id,record.get(SALES.CUSTOMER_ID))
                                                    ).withHref("/customers/view?id="+record.get(SALES.CUSTOMER_ID))
                                            ),
                                            td(record.get(SALES.PRICE_OF_SALE).toString()+" Euro"),
                                            td(record.get(SALES.PRODUCT_OR_SERVICE)),
                                            td(record.get(SALES.TIME_OF_SALE).toLocalDateTime().format(DateTimeFormatter.ISO_DATE)),
                                            td(
                                                    div(
                                                            form(
                                                                    input().withName("id").isHidden().withValue(record.get(SALES.ID).toString()),
                                                                    RecordEditIconUtils.deleteButton()
                                                            ).withAction("/sales?action=delete")
                                                                    .withMethod("post")
                                                                    .withClasses("col-sm-6"),

                                                            form(
                                                                    input().withName("id").isHidden().withValue(record.get(SALES.ID).toString()),
                                                                    RecordEditIconUtils.updateButton()
                                                            ).withAction("/sales/edit")
                                                                    .withMethod("get")
                                                                    .withClasses("col-sm-6")
                                                    ).withClasses("d-flex d-nowrap")
                                            )
                                    )
                    )
            )
    ).withClasses("table");
    return mytable;
  }

  public static ContainerTag makeSalesInsertWidget(int user_id) throws Exception {
    return
            div(
                    div("Add a Sale").withClasses("card-header"),
                    div(
                            form(
                                    div(
                                            div(
                                                    //input().withName("customer_name").withPlaceholder("customer_name").withType("text"),
                                                    div(
                                                            span("Customer:").withClasses("input-group-text")
                                                    ).withClasses("input-group-prepend"),
                                                    makeCustomerSelect(user_id)
                                            ).withClasses("input-group","col"),
                                            div(
                                                    input().withName("price_of_sale")
                                                            .withPlaceholder("price_of_sale")
                                                            .withType("number")
                                                            .attr("min","0")
                                                            .withClasses("form-control"),
                                                    div(
                                                            span("$").withClasses("input-group-text")
                                                    ).withClasses("input-group-append")
                                            ).withClasses("input-group","col")
                                    ).withClasses("row","mb-3"),

                                    div(
                                            div(
                                                    div(
                                                            span("Product or Service").withClasses("input-group-text")
                                                    ).withClasses("input-group-prepend"),
                                                    input().withName("product_or_service").withPlaceholder("product_or_service").withType("text").withClasses("form-control")
                                            ).withClasses("input-group","col"),
                                            div(
                                                    input().withName("time_of_sale").withPlaceholder("time of sale").withType("date").withClasses("form-control")
                                            ).withClasses("input-group","col")
                                    ).withClasses("row","mb-3"),

                                    button("Insert")
                                            .withType("submit")
                                            .withClasses("btn","btn-outline-success","btn-block")


                            ).withAction("/sales?action=insert").withMethod("post")
                    ).withClasses("card-body")
            ).withClasses("m-3","card","mb-4");
  }

  public static ContainerTag makeCustomerSelect(int user_id) throws Exception {

    return
            select(
                    each(
                            CustomersService.getCustomers(user_id),
                            customer->
                                    option(
                                            customer.get(CUSTOMERS.CUSTOMER_NAME)
                                    ).withValue(customer.get(CUSTOMERS.ID)+"")
                    )
            ).withClasses("custom-select").withName("customer_id")
            ;
  }
}
