package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.generated.tables.Projects;
import org.vanautrui.octofinsights.services.ProjectsService;
import org.vanautrui.octofinsights.services.TransactionsService;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public final class ProjectsGanttEndpoint {

	public static Object get(Request req, Response res) {

		if( req.session().attributes().contains("user_id")){
			final int user_id = Integer.parseInt(req.session().attribute("user_id"));

			final Connection conn;
			try {
				conn = DBUtils.makeDBConnection();
			} catch (Exception e) {
				res.status(500);
				res.type(ContentType.TEXT_PLAIN.toString());
				e.printStackTrace();
				return e.getMessage();
			}

			final ObjectMapper mapper = new ObjectMapper();
			final ArrayNode node =  mapper.createArrayNode();

			Result<Record> fetch=null;
			try {
				fetch = ProjectsService.getProjectsByUserId(user_id);

				int i=1;
				for(Record r : fetch){
					final ObjectNode objectNode = mapper.createObjectNode();

					objectNode.put("index",i+"");
					objectNode.put("project_name", r.get(PROJECTS.PROJECT_NAME));
					objectNode.put("start_date",r.get(PROJECTS.PROJECT_START).toString());
					objectNode.put("end_date",r.get(PROJECTS.PROJECT_END).toString());

					node.add(objectNode);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			res.status(200);
			res.type(ContentType.APPLICATION_JSON.toString());
			return node.toPrettyString();
		}else{

			res.status(400);
			res.type(ContentType.TEXT_PLAIN.toString());
			return "Bad Request, no user_id found in session.";
		}
	}
}