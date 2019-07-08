package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaJSONResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.sql.Connection;
import java.sql.Timestamp;

import static org.vanautrui.octofinsights.generated.Tables.*;


public class CashFlowEndpoint extends VaquitaController {
    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest req) throws Exception {

        if(req.session().isPresent() && req.session().get().containsKey("user_id")){
            int user_id = Integer.parseInt(req.session().get().get("user_id"));

            Connection conn= DBUtils.makeDBConnection();
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node =  mapper.createArrayNode();

        /*
        Result<Record2<Integer, String>> result = create.select(
                DSL.coalesce(SALES.PRICE_OF_SALE,EXPENSES.EXPENSE_VALUE).as("value"),
                DSL.coalesce(SALES.PRODUCT_OR_SERVICE,EXPENSES.EXPENSE_NAME).as("label")
        ).from(SALES,EXPENSES).fetch();
         */


            Result<Record3<Integer, String, Timestamp>> result =
                    create.select(
                            (SALES.PRICE_OF_SALE).as("value"),
                            (SALES.PRODUCT_OR_SERVICE).as("label"),
                            (SALES.TIME_OF_SALE).as("time")
                    )
                            .from(SALES).where(SALES.USER_ID.eq(user_id)).orderBy(SALES.TIME_OF_SALE)
                            .union(create.select(
                                    (EXPENSES.EXPENSE_VALUE).as("value"),
                                    (EXPENSES.EXPENSE_NAME).as("label"),
                                    (EXPENSES.EXPENSE_DATE).as("time")
                            )
                                    .from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id))).orderBy(3)
                            .fetch();

            //List<Integer> sales_list = new ArrayList<>();

            for(Record3<Integer, String, Timestamp> r : result){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("value",r.value1());
                objectNode.put("label",r.value2().substring(0,Math.min(r.value2().length(),25)));

                node.add(objectNode);
            }

            conn.close();

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
