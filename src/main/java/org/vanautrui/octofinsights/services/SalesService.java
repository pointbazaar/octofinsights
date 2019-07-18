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

    public static Record getById(int user_id, int sale_id) throws Exception{

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Record record =  create.select(SALES.asterisk()).from(SALES).where(SALES.USER_ID.eq(user_id).and(SALES.ID.eq(sale_id))).fetchOne();
        conn.close();

        return record;
    }

    public static Result<Record> getSales(int user_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

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

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Result<Record> records = create.select(SALES.asterisk()).from(SALES).where(SALES.USER_ID.eq(user_id).and(SALES.TIME_OF_SALE.between(date1,date2))).fetch().sortDesc(SALES.TIME_OF_SALE);
        conn.close();

        return records;
    }

    public static long getTotalForThisMonth(int user_id)throws Exception{
        return getSalesThisMonth(user_id).stream().map(sale->sale.get(SALES.PRICE_OF_SALE).longValue()).reduce(Long::sum).orElse(0L);
    }

    public static void updateById(int user_id, int sale_id, String customer_name, int price_of_sale, Timestamp time_of_sale, String product_or_service) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        create
                .update(SALES)
                .set(SALES.CUSTOMER_NAME,customer_name)
                .set(SALES.PRICE_OF_SALE,price_of_sale)
                .set(SALES.TIME_OF_SALE,time_of_sale)
                .set(SALES.PRODUCT_OR_SERVICE,product_or_service)
                .where(SALES.USER_ID.eq(user_id).and(SALES.ID.eq(sale_id))).execute();
        conn.close();
    }
}
