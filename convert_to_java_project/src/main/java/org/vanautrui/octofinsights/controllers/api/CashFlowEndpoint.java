package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.vaquitamvc.controller.VaquitaJSONController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaJSONResponse;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.List;

import static org.vanautrui.octofinsights.generated.Tables.*;


public class CashFlowEndpoint extends VaquitaJSONController {
    @Override
    public VaquitaJSONResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {

        List<String> credentials = Files.readAllLines(Paths.get("credentials.txt"));

        String userName = credentials.get(0);
        String password = credentials.get(1);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node =  mapper.createArrayNode();

        String url = "jdbc:mysql://vanautrui.org:3306/octofinsights";

        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        Connection conn = DriverManager.getConnection(url, userName, password);

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        //Result<Record> result = create.select().from(SALES).fetch();

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
                        .from(SALES)
                        .union(create.select(
                                (EXPENSES.EXPENSE_VALUE).as("value"),
                                (EXPENSES.EXPENSE_NAME).as("label"),
                                (EXPENSES.EXPENSE_DATE).as("time")
                        )
                                .from(EXPENSES)).orderBy(3)
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
    }

    @Override
    public VaquitaJSONResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        return null;
    }
}
