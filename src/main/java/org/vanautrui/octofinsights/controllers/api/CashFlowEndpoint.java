package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.services.TransactionsService;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;


public final class CashFlowEndpoint {

	public static Object get(Request req, Response res) {

		if (req.session().attributes().contains("user_id")) {
			int user_id = Integer.parseInt(req.session().attribute("user_id"));

			Connection conn = null;
			try {
				conn = DBUtils.makeDBConnection();
			} catch (Exception e) {
				res.status(500);
				res.type(ContentType.TEXT_PLAIN.toString());
				e.printStackTrace();
				return e.getMessage();
			}
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode node = mapper.createArrayNode();

			final int current_year = LocalDateTime.now().getYear();

			Result<Record2<BigDecimal, Integer>> result = null;
			try {
				result = TransactionsService.getAllTransactionsForUserIdInThisYearGroupedByMonth(user_id);
			} catch (Exception e) {
				e.printStackTrace();
			}


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
                  //this month is not present in the db. month count > m
                  //we have to fill in a value for this month
                  node.add(makeMonthlyRecord(new BigDecimal("0.0"), month_str + " " + current_year, month_count));
                }

			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			res.status(200);
			res.type(ContentType.APPLICATION_JSON.toString());
			return node.toPrettyString();
		} else {

			res.status(400);
			res.type(ContentType.TEXT_PLAIN.toString());
			return "Bad Request, no user_id found in session.";
		}
	}

	private static ObjectNode makeMonthlyRecord(final BigDecimal value, final String label, final int month_count) {
		final ObjectNode obj = (new ObjectMapper()).createObjectNode();

		obj.put("value", value);
		obj.put("label", label);
		obj.put("month", month_count);

		return obj;
	}
}
