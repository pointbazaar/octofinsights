package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.vaquitamvc.controller.VaquitaJSONController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaJSONResponse;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
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
        Result<Record> result = create.select().from(SALES).fetch();

        //List<Integer> sales_list = new ArrayList<>();

        for(Record r : result){
            node.add(r.getValue(SALES.PRICE_OF_SALE));
        }

        conn.close();

        return new VaquitaJSONResponse(200,node);
    }

    @Override
    public VaquitaJSONResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        return null;
    }
}
