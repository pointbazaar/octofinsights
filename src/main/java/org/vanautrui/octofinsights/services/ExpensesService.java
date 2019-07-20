package org.vanautrui.octofinsights.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.generated.tables.Expenses;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.Date;

import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public class ExpensesService {

    public static Result<Record> getExpenses(int user_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node =  mapper.createArrayNode();

        //Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortAsc(LEADS.LEAD_STATUS.startsWith("open"));
        Result<Record> records = create.select(EXPENSES.asterisk()).from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id)).fetch().sortDesc(EXPENSES.EXPENSE_DATE);
        conn.close();

        return records;
    }

    public static long getTotal(int user_id) throws Exception{
        return getExpenses(user_id).stream().map(sale->sale.get(EXPENSES.EXPENSE_VALUE).longValue()).reduce(Long::sum).orElse(0L);
    }

    public static void delete(int id,int user_id)throws Exception{
        Connection conn= DBUtils.makeDBConnection();

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        create.deleteFrom(Expenses.EXPENSES).where( (Expenses.EXPENSES.ID).eq(id).and((Expenses.EXPENSES.USER_ID).eq(user_id)) ).execute();

        conn.close();
    }

    public static void insert(String expense_name, Timestamp expense_date_timestamp, int expense_value, int user_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        create
                .insertInto(Expenses.EXPENSES)
                .columns(Expenses.EXPENSES.EXPENSE_NAME, Expenses.EXPENSES.EXPENSE_DATE, Expenses.EXPENSES.EXPENSE_VALUE, Expenses.EXPENSES.USER_ID)
                .values(expense_name,expense_date_timestamp,expense_value,user_id).execute();

        conn.close();
    }

    public static Result<Record> getExpensesThisMonth(int user_id)throws Exception{

        Timestamp date1 = Timestamp.valueOf(YearMonth.now().atDay(1).atStartOfDay());

        Timestamp date2 = Timestamp.valueOf(YearMonth.now().atEndOfMonth().plusDays(1).atStartOfDay());

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Result<Record> records = create.select(EXPENSES.asterisk()).from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id).and(EXPENSES.EXPENSE_DATE.between(date1,date2))).fetch().sortDesc(EXPENSES.EXPENSE_DATE);
        conn.close();

        return records;
    }

    public static long getTotalForThisMonth(int user_id)throws Exception{
        return getExpensesThisMonth(user_id).stream().map(sale->sale.get(EXPENSES.EXPENSE_VALUE).longValue()).reduce(Long::sum).orElse(0L);
    }

    public static Record getById(int user_id, int expense_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Record record = create.select(EXPENSES.asterisk()).from(EXPENSES).where(EXPENSES.USER_ID.eq(user_id).and(EXPENSES.ID.eq(expense_id))).fetchOne();
        conn.close();

        return record;
    }

    public static void updateById(int user_id, int expense_id, String expense_name, int price, Timestamp expense_date_timestamp) throws Exception{

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        create
                .update(EXPENSES)
                .set(EXPENSES.EXPENSE_NAME,expense_name)
                .set(EXPENSES.EXPENSE_VALUE,price)
                .set(EXPENSES.EXPENSE_DATE,expense_date_timestamp)

                .where(EXPENSES.USER_ID.eq(user_id).and(EXPENSES.ID.eq(expense_id))).execute();
        conn.close();
    }
}
