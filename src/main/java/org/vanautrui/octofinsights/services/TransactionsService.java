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
		try(final Connection conn= DBUtils.makeDBConnection()) {
			final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

			final int current_year = LocalDateTime.now().getYear();

			final var records = ctx
					.select(sum(TRANSACTIONS_WITH_USER.VALUE.as("value")),month(TRANSACTIONS_WITH_USER.TIME).as("month"))
					.from(TRANSACTIONS_WITH_USER)
					.where(TRANSACTIONS_WITH_USER.USER_ID.eq(user_id).and(year(TRANSACTIONS_WITH_USER.TIME).eq(current_year)))
					.groupBy(month(TRANSACTIONS_WITH_USER.TIME))
					.fetch();

			return records;
		}
	}

	public static Result<Record3<BigDecimal,Integer,Integer>> getAllTransactionsForUserIdGroupedByMonthAndYear(final int user_id) throws Exception{
		try(final Connection conn= DBUtils.makeDBConnection()) {
			final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

			//final int current_year = LocalDateTime.now().getYear();

			final var records = ctx
					.select(sum(TRANSACTIONS_WITH_USER.VALUE.as("value")),month(TRANSACTIONS_WITH_USER.TIME).as("month"),year(TRANSACTIONS_WITH_USER.TIME).as("year"))
					.from(TRANSACTIONS_WITH_USER)
					.where(TRANSACTIONS_WITH_USER.USER_ID.eq(user_id))
					.groupBy(year(TRANSACTIONS_WITH_USER.TIME),month(TRANSACTIONS_WITH_USER.TIME))
					.orderBy(year(TRANSACTIONS_WITH_USER.TIME),month(TRANSACTIONS_WITH_USER.TIME))
					.fetch();
			

			return records;
		}
	}

	public static ArrayNode getTransactionsForTheYear(final int user_id, final boolean include_month_with_no_data) throws Exception{
		final ObjectMapper mapper = new ObjectMapper();
		final ArrayNode node = mapper.createArrayNode();

		final int current_year = LocalDateTime.now().getYear();

		final Result<Record2<BigDecimal, Integer>> result =
				TransactionsService.getAllTransactionsForUserIdInThisYearGroupedByMonth(user_id);


		for (int m = 1; m <= 12; m++) {

			final Month month_no = LocalDateTime.of(current_year, m, 1, 1, 1).getMonth();
			final int month_count = month_no.getValue(); // elem [1,12]
			final String month_str = month_no.toString();

			//if our month is within the result and equal to the month at that index
			if(result != null && m <= result.size() && result.get(m-1).value2() == m){

				//result is 0-indexed
				final Record2<BigDecimal, Integer> r = result.get(m-1);
				//correct. this month is present in the DB
				node.add(makeMonthlyRecord(r.value1(), month_str + " " + current_year, month_count));
			}else{
				if(include_month_with_no_data) {
					//this month is not present in the db. month count > m
					//we have to fill in a value for this month
					node.add(makeMonthlyRecord(new BigDecimal("0.0"), month_str + " " + current_year, month_count));
				}
			}

		}

		return node;
	}

	private static ObjectNode makeMonthlyRecord(final BigDecimal value, final String label, final int month_count) {
		final ObjectNode obj = (new ObjectMapper()).createObjectNode();

		obj.put("value", value);
		obj.put("label", label);
		obj.put("month", month_count);

		return obj;
	}
}
