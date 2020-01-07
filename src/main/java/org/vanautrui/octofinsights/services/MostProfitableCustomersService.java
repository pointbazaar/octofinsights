package org.vanautrui.octofinsights.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.Month;

import static org.jooq.impl.DSL.*;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Sales.SALES;

public final class MostProfitableCustomersService {

	public static Result<Record2<BigDecimal,String>> getByUserId(final int user_id) throws Exception{

		try(final Connection conn= DBUtils.makeDBConnection()) {
			final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

			final var records = ctx
					.select(sum(SALES.PRICE_OF_SALE).as("value"),CUSTOMERS.CUSTOMER_NAME)
					.from(SALES).join(CUSTOMERS).on(SALES.CUSTOMER_ID.eq(CUSTOMERS.ID))
					.where(SALES.USER_ID.eq(user_id).and(CUSTOMERS.USER_ID.eq(user_id)))
					.groupBy(CUSTOMERS.ID)
					.orderBy(one().desc())
					.limit(4)
					.fetch();


			return records;
		}
	}

}
