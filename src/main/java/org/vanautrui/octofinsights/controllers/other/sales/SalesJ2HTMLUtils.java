package org.vanautrui.octofinsights.controllers.other.sales;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils;
import org.vanautrui.octofinsights.services.CustomersService;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static j2html.TagCreator.*;
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
                                            td(record.get(SALES.PRICE_OF_SALE).toString()+" €"),
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
}
