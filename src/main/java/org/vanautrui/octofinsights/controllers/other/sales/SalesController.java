package org.vanautrui.octofinsights.controllers.other.sales;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.NavigationUtil;
import org.vanautrui.octofinsights.services.CustomersService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVFullController;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPostRequest;
import org.vanautrui.vaquitamvc.requests.VHTTPPutRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static j2html.TagCreator.*;
import static java.lang.Integer.parseInt;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeSalesInsertWidget;
import static org.vanautrui.octofinsights.controllers.other.sales.SalesJ2HTMLUtils.makeSalesTable;

public class SalesController implements IVFullController {

    @Override
    public IVHTTPResponse handleGET(VHTTPGetRequest request, VApp app) throws Exception {
        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true")
                && request.session().get().containsKey("user_id")
        ){
            final int user_id = parseInt(request.session().get().get("user_id"));

            final List<Record> records = SalesService.getSales(user_id);

            final ContainerTag mytable = makeSalesTable(user_id,records);

            final List<Record> all_customers = CustomersService.getCustomers(user_id);

            if(all_customers.size()==0){
                return new VTextResponse(200,"Please first create a Customer, to view the Sales Section ");
            }

            final String page=
                    html(
                            HeadUtil.makeHead(),
                            body(
                                    NavigationUtil.createNavbar(request.session().get().get("username"),"Sales"),
                                    div(
                                            div(
                                                    makeSalesInsertWidget(user_id),
                                                    mytable
                                            ).withId("main-content")
                                    ).withClasses("container")

                            )
                    ).render();

            return new VHTMLResponse(200,page);

        }else {
            return new VRedirectToGETResponse("/login", request);
        }
    }

    @Override
    public IVHTTPResponse handlePOST(VHTTPPostRequest entityReq, VApp vApp) throws Exception {

        if( entityReq.session().isPresent() && entityReq.session().get().containsKey("authenticated") && entityReq.session().get().get("authenticated").equals("true")
                && entityReq.session().get().containsKey("user_id")
        ) {

            final int user_id = parseInt(entityReq.session().get().get("user_id"));

            final String action = entityReq.getQueryParam("action");

            if(action.equals("delete") && entityReq.getPostParameters().containsKey("id")){

                int id = parseInt(entityReq.getPostParameters().get("id"));
                SalesService.deleteById(id,user_id);
            }else if(action.equals("insert")
                    && entityReq.getPostParameters().containsKey("customer_id")
                    && entityReq.getPostParameters().containsKey("product_or_service")
                    && entityReq.getPostParameters().containsKey("price_of_sale")
            ){

                final int customer_id = parseInt(entityReq.getPostParameters().get("customer_id"));
                final String product_or_service= entityReq.getPostParameters().get("product_or_service");
                final int price= parseInt(entityReq.getPostParameters().get("price_of_sale"));
                final String time_of_sale = entityReq.getPostParameters().get("time_of_sale");

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time_of_sale);

                Timestamp date_of_sale = new Timestamp(date.getTime());

                SalesService.insert(user_id,customer_id,product_or_service,price,date_of_sale);
            }

            return new VRedirectToGETResponse("/sales",entityReq);
        }else {
            return new VRedirectToGETResponse("/login",entityReq);
        }
    }

    @Override
    public IVHTTPResponse handlePUT(VHTTPPutRequest vhttpPutRequest, VApp vApp) throws Exception {
        return null;
    }
}
