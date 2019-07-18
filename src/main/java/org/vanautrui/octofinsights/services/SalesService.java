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
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.Date;

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

    public static long getTotal(int user_id) throws Exception{
        return getSales(user_id).stream().map(sale->sale.get(SALES.PRICE_OF_SALE).longValue()).reduce(Long::sum).orElse(0L);
    }

    public static Result<Record> getSalesThisMonth(int user_id)throws Exception{

        Timestamp date1 = Timestamp.valueOf(YearMonth.now().atDay(1).atStartOfDay());

        Timestamp date2 = Timestamp.valueOf(YearMonth.now().atEndOfMonth().plusDays(1).atStartOfDay());

        //System.out.println(date1.toString()+","+date2.toString());

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node =  mapper.createArrayNode();

        //Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortAsc(LEADS.LEAD_STATUS.startsWith("open"));
        Result<Record> records = create.select(SALES.asterisk()).from(SALES).where(SALES.USER_ID.eq(user_id).and(SALES.TIME_OF_SALE.between(date1,date2))).fetch().sortDesc(SALES.TIME_OF_SALE);
        conn.close();

        return records;
    }

    public static long getTotalForThisMonth(int user_id)throws Exception{
        return getSalesThisMonth(user_id).stream().map(sale->sale.get(SALES.PRICE_OF_SALE).longValue()).reduce(Long::sum).orElse(0L);
    }
}
