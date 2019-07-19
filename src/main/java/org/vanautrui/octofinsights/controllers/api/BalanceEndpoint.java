package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.util.Pair;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.ILoggerFactory;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.services.ExpensesService;
import org.vanautrui.octofinsights.services.SalesService;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaJSONResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.sql.Connection;
import java.sql.Timestamp;

import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.Tables.SALES;


public class BalanceEndpoint extends VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest req) throws Exception {

        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));


            long balance = SalesService.getTotal(user_id)+ ExpensesService.getTotal(user_id);

            //Pair<String, Long> pair = new Pair<String, Long>("balance",balance);

            ObjectNode node = (new ObjectMapper()).createObjectNode();

            node.put("balance",balance);

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
