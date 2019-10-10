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

import java.util.concurrent.atomic.AtomicLong;


public class ProfitEndpoint implements IVGETHandler {
    @Override
    public IVHTTPResponse handleGET(VHTTPGetRequest req, VApp vApp) throws Exception {
        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            final int user_id = Integer.parseInt(req.session().get().get("user_id"));

            final AtomicLong x1 = new AtomicLong(0);
            final AtomicLong x2 = new AtomicLong(0);

            //https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        x1.set(SalesService.getTotalForThisMonth(user_id));
                    } catch (Exception e) {
                        //
                    }
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        x2.set(ExpensesService.getTotalForThisMonth(user_id));
                    } catch (Exception e) {
                        //
                    }
                }
            });

            //start both db queries
            t1.run();
            t2.run();

            //wait for both to finish
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
