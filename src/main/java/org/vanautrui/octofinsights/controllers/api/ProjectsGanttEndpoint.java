package org.vanautrui.octofinsights.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.jooq.Record;
import org.jooq.Result;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.services.ProjectsService;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.SQLException;

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
				fetch = ProjectsService.getActiveProjectsByUserId(user_id);

				int i=1;
				for(Record r : fetch){
					final ObjectNode obj = mapper.createObjectNode();

					obj.put("index",i+"");
					obj.put("project_name", r.get(PROJECTS.PROJECT_NAME));
					obj.put("start_date",r.get(PROJECTS.PROJECT_START).toString());
					obj.put("end_date",r.get(PROJECTS.PROJECT_END).toString());


					final int start_month = r.get(PROJECTS.PROJECT_START).getMonth();
					final int end_month = r.get(PROJECTS.PROJECT_END).getMonth();
					obj.put("start_month",start_month);
					obj.put("end_month",end_month);

					node.add(obj);
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
