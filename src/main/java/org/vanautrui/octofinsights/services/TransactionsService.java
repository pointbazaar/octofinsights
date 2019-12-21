package org.vanautrui.octofinsights.services;

import org.jooq.*;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;

import static org.vanautrui.octofinsights.generated.tables.TransactionsWithUser.TRANSACTIONS_WITH_USER;

public final class TransactionsService {

	//All transactions with user id (table 'transactions_with_user')
	/*
	select id "id",sales.price_of_sale "value",sales.time_of_sale "time", user_id "user_id"
		from sales
		union(
				select id "id", expense_value "value" ,expense_date "time", user_id "user_id"
				from expenses
			)
		order by value asc;

	 */


	public static Result<Record2<BigDecimal, Integer>> getAllTransactionsForUserIdInThisYearGroupedByMonth(final int user_id) throws Exception {
		try(Connection conn= DBUtils.makeDBConnection()) {
			final DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			final int current_year = LocalDateTime.now().getYear();

			final var records = create
					.select(sum(TRANSACTIONS_WITH_USER.VALUE.as("value")),month(TRANSACTIONS_WITH_USER.TIME).as("month"))
					.from(TRANSACTIONS_WITH_USER)
					.where(TRANSACTIONS_WITH_USER.USER_ID.eq(user_id).and(year(TRANSACTIONS_WITH_USER.TIME).eq(current_year)))
					.groupBy(month(TRANSACTIONS_WITH_USER.TIME))
					.fetch();

			return records;
		}
	}
}
