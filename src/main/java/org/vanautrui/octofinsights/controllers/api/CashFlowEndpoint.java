package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.entity.ContentType;
import org.vanautrui.octofinsights.services.TransactionsService;
import spark.Request;
import spark.Response;


public final class CashFlowEndpoint {

	public static Object get(Request req, Response res) {

		final boolean fill = Boolean.parseBoolean(req.queryParamOrDefault("fill","false"));

		if (req.session().attributes().contains("user_id")) {
			final int user_id = Integer.parseInt(req.session().attribute("user_id"));

			ArrayNode node = null;

			try {
              node = TransactionsService.getTransactionsForTheYear(user_id,fill);
            }catch (Exception e){
			  e.printStackTrace();
			  res.status(500);
			  return e.getMessage();
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


}
