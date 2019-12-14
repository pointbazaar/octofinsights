package org.vanautrui.octofinsights.services;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.generated.tables.Expenses;
import org.vanautrui.octofinsights.generated.tables.Invoices;

import java.sql.Connection;

import static org.vanautrui.octofinsights.generated.tables.Invoices.INVOICES;

public final class InvoicesService {

    public static void insertInvoiceItem(final int user_id, final int customer_id,final String product_or_service,final int price)throws Exception{
        /*
            id int
            user_id int
            customer_id int
            product_or_service varchar
            price int
         */
        try(final Connection conn= DBUtils.makeDBConnection()) {

            final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
            ctx.insertInto(INVOICES).columns(INVOICES.USER_ID,INVOICES.CUSTOMER_ID,INVOICES.PRODUCT_OR_SERVICE,INVOICES.PRICE).values(user_id,customer_id,product_or_service,price).execute();
        }
    }

    public static Result<Record> getAllInvoicItemsForUserId(final int user_id)throws Exception{
        try(final Connection conn= DBUtils.makeDBConnection()) {

            final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
            final Result<Record> fetch = ctx.select(INVOICES.asterisk()).from(INVOICES).where(INVOICES.USER_ID.eq(user_id)).fetch();

            return fetch;
        }
    }

    public static void deleteInvoiceItemById(final int id) throws Exception{
        try(final Connection conn= DBUtils.makeDBConnection()) {

            final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
            ctx.deleteFrom(INVOICES).where(INVOICES.ID.eq(id)).execute();
        }
    }
}
