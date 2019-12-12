package org.vanautrui.octofinsights.services;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public final class CustomersService {

    //https://www.youtube.com/watch?v=dlgT8kcAuvk&list=RDEMYGj5tu94_mNz6SrYkDD3_g&index=27

    private static Map<Integer,String> customerNameCache=new HashMap<>();

    private static synchronized String getCustomerNameForIdCached(int user_id, int customer_id){
        if(customerNameCache.containsKey(customer_id)){
            return customerNameCache.get(customer_id);
        }else{
            try {
                String name = getCustomerNameForIdDB(user_id, customer_id);
                //cache our result
                customerNameCache.put(customer_id,name);

                return getCustomerNameForId(user_id,customer_id);
            }catch (Exception e) {
                e.printStackTrace();

                //do not cache this result
                return "customer not known. fatal error";
            }
        }
    }

    public static String getCustomerNameForId(int user_id,int customer_id){
        return getCustomerNameForIdCached(user_id,customer_id);
    }

    private static String getCustomerNameForIdDB(int user_id,int customer_id)throws Exception{
        //this needs to be cached since this query potentially runs in a loop
        //but by implementing the services pattern, all access to the customer table runs through
        //this service class. this makes caching easier, since we know when the name has been updated,
        //and we have to remove that name from the cache

        try(Connection conn = DBUtils.makeDBConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

            Record1<String> name = create.select(CUSTOMERS.CUSTOMER_NAME).from(CUSTOMERS).where(CUSTOMERS.USER_ID.eq(user_id).and(CUSTOMERS.ID.eq(customer_id))).fetchOne();

            return name.component1();
        }
    }

    public static List<Record> getCustomers(int user_id) throws Exception {

        try (Connection conn = DBUtils.makeDBConnection()){

            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            List<Record> list = ctx
                    .select(CUSTOMERS.asterisk())
                    .from(CUSTOMERS)
                    .where(CUSTOMERS.USER_ID.eq(user_id))
                    .fetch()
                    .stream()
                    .collect(Collectors.toList());

            return list;
        }
    }

    public static void insertCustomer(int user_id, String customer_name, String customer_source)throws Exception{
        try(Connection conn= DBUtils.makeDBConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            ctx.insertInto(CUSTOMERS)
                    .columns(CUSTOMERS.CUSTOMER_NAME, CUSTOMERS.USER_ID, CUSTOMERS.SOURCE)
                    .values(customer_name, user_id, customer_source)
                    .execute();
        }
    }

    public static Record getCustomerById(int user_id,int customer_id) throws Exception{
        try(Connection conn= DBUtils.makeDBConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            Record customer = ctx.select(CUSTOMERS.asterisk())
                    .from(CUSTOMERS)
                    .where(CUSTOMERS.ID.eq(customer_id).and(CUSTOMERS.USER_ID.eq(user_id)))
                    .fetchOne();

            return customer;
        }
    }
}
