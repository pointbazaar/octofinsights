package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.vanautrui.octofinsights.services.ExpensesService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.VApp;
import org.vanautrui.vaquitamvc.controller.IVGETHandler;
import org.vanautrui.vaquitamvc.requests.VHTTPGetRequest;
import org.vanautrui.vaquitamvc.responses.IVHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VJsonResponse;
import org.vanautrui.vaquitamvc.responses.VTextResponse;
import spark.Request;
import spark.Response;

import java.util.concurrent.atomic.AtomicLong;


public final class BalanceEndpoint {


    public static Response get(Request request, Response response) {
        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            AtomicLong x1 = new AtomicLong(0);
            AtomicLong x2 = new AtomicLong(0);

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        x1.set(SalesService.getTotal(user_id));
                    } catch (Exception e) {
                        //TODO
                    }
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        x2.set(ExpensesService.getTotal(user_id));
                    } catch (Exception e) {
                        //TODO
                    }
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            final long balance = x1.get()+x2.get();

            final ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("value",balance);

            return new VJsonResponse(200,node);
        }else{
            return new VTextResponse(400, "Bad Request, no user_id found in session.");
        }
    }
}
