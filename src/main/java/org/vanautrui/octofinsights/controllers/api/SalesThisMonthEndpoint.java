package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaJSONResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;


public class SalesThisMonthEndpoint extends VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest req) throws Exception {

        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));


            long balance = SalesService.getTotalForThisMonth(user_id);

            ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("value",balance);

            return new VaquitaJSONResponse(200,node);
        }else{
            return new VaquitaTextResponse(400, "Bad Request, no user_id found in session.");
        }
    }

    @Override
    public VaquitaJSONResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        return null;
    }
}
