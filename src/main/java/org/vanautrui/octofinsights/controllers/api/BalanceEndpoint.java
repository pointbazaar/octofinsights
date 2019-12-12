package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.ExpensesService;
import org.vanautrui.octofinsights.services.SalesService;
import spark.Request;
import spark.Response;

import java.util.concurrent.atomic.AtomicLong;


public final class BalanceEndpoint {


    public static Object get(Request req, Response res) {
        if( req.session().attributes().contains("user_id")){
            int user_id = Integer.parseInt(req.session().attribute("user_id"));

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

            try {
                t1.join();
                t2.join();
            }catch (Exception e){
                //pass
            }

            final long balance = x1.get()+x2.get();

            final ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("value",balance);

            res.status(200);
            res.type(ContentType.APPLICATION_JSON.toString());
            return node.toPrettyString();
        }else{

            res.status(400);
            res.type(ContentType.TEXT_PLAIN.toString());
            return "Bad Request, no user_id found in session.";
        }
    }
}
