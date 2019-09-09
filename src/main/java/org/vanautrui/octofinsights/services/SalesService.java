package org.vanautrui.octofinsights.services;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.YearMonth;

import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class SalesService {

    //https://www.youtube.com/watch?v=WOBGQvzBDqI&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=23

    public static String getCustomerNameForId(int user_id,int customer_id){

        try {
            Connection conn = DBUtils.makeDBConnection();
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

            Record1<String> name = create.select(CUSTOMERS.CUSTOMER_NAME).from(CUSTOMERS).where(CUSTOMERS.USER_ID.eq(user_id).and(CUSTOMERS.ID.eq(customer_id))).fetchOne();

            conn.close();
            return name.component1();
        }catch (Exception e){
            e.printStackTrace();
            return "customer not known. fatal error";
        }
    }

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

        Result<Record> records = create
                .select(SALES.asterisk())
                .from(SALES)
                .where(SALES.USER_ID.eq(user_id))
                .fetch()
                .sortDesc(SALES.TIME_OF_SALE);
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

    public static void updateById(int user_id, int sale_id, int customer_id, int price_of_sale, Timestamp time_of_sale, String product_or_service) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        create
                .update(SALES)
                .set(SALES.CUSTOMER_ID,customer_id)
                .set(SALES.PRICE_OF_SALE,price_of_sale)
                .set(SALES.TIME_OF_SALE,time_of_sale)
                .set(SALES.PRODUCT_OR_SERVICE,product_or_service)
                .where(SALES.USER_ID.eq(user_id).and(SALES.ID.eq(sale_id))).execute();
        conn.close();
    }

    public static void deleteById(int id, int user_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        create.deleteFrom(SALES).where( (SALES.ID).eq(id).and((SALES.USER_ID).eq(user_id)) ).execute();

        conn.close();
    }

    public static void insert(int user_id,int customer_id,String product_or_service,int price,Timestamp date_of_sale) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        ctx
                .insertInto(SALES)
                .columns(SALES.CUSTOMER_ID,SALES.PRODUCT_OR_SERVICE,SALES.PRICE_OF_SALE, SALES.USER_ID,SALES.TIME_OF_SALE)
                .values(customer_id,product_or_service,price,user_id,date_of_sale).execute();

        conn.close();
    }
}
