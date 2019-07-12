package org.vanautrui.octofinsights.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;

import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class SalesService {

    public static Result<Record> getSales(int user_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node =  mapper.createArrayNode();

        //Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortAsc(LEADS.LEAD_STATUS.startsWith("open"));
        Result<Record> records = create.select(SALES.asterisk()).from(SALES).where(SALES.USER_ID.eq(user_id)).fetch().sortDesc(SALES.TIME_OF_SALE);
        conn.close();

        return records;
    }
}
