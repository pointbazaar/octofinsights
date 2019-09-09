package org.vanautrui.octofinsights.services;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;

public class CustomersService {

    public static List<Record> getCustomers(int user_id){
        try {
            Connection conn = DBUtils.makeDBConnection();
            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            List<Record> list = ctx
                    .select(CUSTOMERS.asterisk())
                    .from(CUSTOMERS)
                    .where(CUSTOMERS.USER_ID.eq(user_id))
                    .fetch()
                    .stream()
                    .collect(Collectors.toList());

            conn.close();
            return list;
        }catch (Exception e){
            e.printStackTrace();
            //fatal error
            return new ArrayList<>();
        }
    }

    public static void insertCustomer(int user_id, String customer_name, String customer_source)throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        ctx.insertInto(CUSTOMERS)
                .columns(CUSTOMERS.CUSTOMER_NAME,CUSTOMERS.USER_ID,CUSTOMERS.SOURCE)
                .values(customer_name,user_id,customer_source)
                .execute();

        conn.close();
    }

}
