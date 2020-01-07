package org.vanautrui.octofinsights.controllers.other.invoices;

import j2html.tags.ContainerTag;
import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.jooq.Result;
import org.vanautrui.octofinsights.controllers.other.customers.CustomersJ2HTMLUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.ErrorUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.InvoicesService;
import spark.Request;
import spark.Response;

import java.util.stream.Collectors;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Invoices.INVOICES;
import static org.vanautrui.octofinsights.html_util_domain_specific.RecordEditIconUtils.deleteButton;

public final class InvoicesController {

    //https://codeburst.io/generate-pdf-invoices-with-javascript-c8dbbfb56361

    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ){

            final int user_id = Integer.parseInt(req.session().attribute("user_id"));

            int totalAmountOwed = 0;

            Result<Record> invoiceItems=null;
            ContainerTag invoiceList = div("error retrieving invoice items");
            try {
                invoiceItems = InvoicesService.getAllInvoicItemsForUserId(user_id);

                totalAmountOwed = invoiceItems
                        .stream()
                        .map(record -> record.get(INVOICES.PRICE))
                        .reduce(Integer::sum)
                        .orElseGet(()->0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(invoiceItems!=null){
                invoiceList=ul(
                        invoiceItems
                                .stream()
                                .map(
                                        r-> {
                                            try {
                                                return li(
                                                    div(
                                                        div(
                                                                CustomersJ2HTMLUtils.createLinkToCustomer(user_id,r.get(INVOICES.CUSTOMER_ID))
                                                        ).withClasses("col"),
                                                        div(
                                                        " owes "+
                                                                r.get(INVOICES.PRICE)+" Euro" +
                                                                " for "+r.get(INVOICES.PRODUCT_OR_SERVICE)
                                                        ).withClasses("col"),
                                                        div(
                                                            form(
                                                                    input().isHidden().withName("id").withValue(r.get(INVOICES.ID).toString()),
                                                                    deleteButton().withType("submit")
                                                            )
                                                                    .withClasses("form-inline")
                                                                    .withMethod("post")
                                                                    .withAction("/invoices?action=delete")
                                                        ).withClasses("col")

                                                        //TODO: have option to convert the invoice item to a sale, if the customer has paid it
                                                    ).withClasses("row")
                                                ).withClasses("list-group-item");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                return "ERROR "+e.getMessage();
                                            }
                                        }
                                )
                                .collect(Collectors.toList())
                                .toArray(new ContainerTag[]{})
                ).withId("invoice-list")
                .withClasses("list-group");
            }

            final ContainerTag invoiceItemInsertWidget;
            try {
                invoiceItemInsertWidget = InvoicesJ2HTMTLUtils.makeInvoiceItemInsertWidget(user_id);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return ErrorUtil.errorHTML("internal app error. could not create widget for invoice item insertion. "+e.getMessage());
            }

            String page=
                    html(
                            HeadUtil.makeHead(
                                    //jspdf probably not needed anymore. pdf can be generated on server side.
                                    //TODO: generate pdf on server side
                                    script()
                                            .withSrc("https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js")
                                            .attr("crossorigin","anonymous")
                            ),
                            body(
                                    NavigationUtil.createNavbar(req.session().attribute("username"),"Invoices"),
                                    div(
                                            div(
                                                    invoiceItemInsertWidget,
                                                    p("Total amount owed to you: "+totalAmountOwed+" $"),
                                                    button("Generate PDF (TODO)")
                                                            .withId("generateButton")
                                                            .withClasses("btn btn-primary"),
                                                    hr(),
                                                    invoiceList
                                            ).withId("main-content")
                                    ).withClasses("container")
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

    public static Object post(Request req, Response res) {
        if( req.session().attributes().contains("authenticated")
                && req.session().attribute("authenticated").equals("true")
                && req.session().attributes().contains("user_id")
        ) {

            final int user_id = Integer.parseInt(req.session().attribute("user_id"));

            final String action = req.queryParams("action");

            if(action.equals("insert")
                    && req.queryParams().contains("customer_id")
                    && req.queryParams().contains("price")
                    && req.queryParams().contains("product_or_service")
            ){

                final String product_or_service = req.queryParams("product_or_service");
                final int price = Integer.parseInt(req.queryParams("price"));
                final int customer_id = Integer.parseInt(req.queryParams("customer_id"));

                try {
                    InvoicesService.insertInvoiceItem(user_id,customer_id,product_or_service,price);
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }else if(action.equals("delete")){
                final int id = Integer.parseInt(req.queryParams("id"));
                try{
                    InvoicesService.deleteInvoiceItemById(id);
                }catch (Exception e){
                    e.printStackTrace();
                    res.status(500);
                    res.type(ContentType.TEXT_PLAIN.toString());
                    return e.getMessage();
                }
            }

            res.redirect("/invoices");
        }else {
            res.redirect("/login");
        }
        return "";
    }
}
