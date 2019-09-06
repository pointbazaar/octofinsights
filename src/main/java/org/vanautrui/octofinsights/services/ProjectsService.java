package org.vanautrui.octofinsights.services;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;
import java.sql.Timestamp;

import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;

public class ProjectsService {

    public static Result<Record> getProjectsByUserId(int user_id)throws Exception{

        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        Result<Record> myprojects = ctx.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.USER_ID.eq(user_id)).fetch();

        conn.close();

        return myprojects;
    }

    public static void insertProject(
            int user_id,
            String project_name, Timestamp project_start,
            Timestamp project_end_estimate,
            /*Timestamp project_end,*/
            int initial_effort_estimate_hours,
            int project_earnings_estimate,
            String project_description
    )throws Exception{
        byte isActive=1;
        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        ctx.insertInto(PROJECTS)
                .columns(
                        PROJECTS.USER_ID,
                        PROJECTS.PROJECT_NAME,
                        PROJECTS.PROJECT_DESCRIPTION,
                        PROJECTS.PROJECT_START,
                        PROJECTS.PROJECT_END_ESTIMATE,
                        PROJECTS.INITIAL_EFFORT_ESTIMATE_HOURS,
                        PROJECTS.PROJECT_EARNINGS_ESTIMATE,
                        PROJECTS.ISACTIVE
                )
                .values(
                        user_id,
                        project_name,
                        project_description,
                        project_start,
                        project_end_estimate,
                        initial_effort_estimate_hours,
                        project_earnings_estimate,
                        isActive
                )
                .execute();
        conn.close();
    }

    public static Record getById(int user_id, int sale_id) throws Exception{
        //TODO
        /*
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Record record =  create.select(SALES.asterisk()).from(SALES).where(SALES.USER_ID.eq(user_id).and(SALES.ID.eq(sale_id))).fetchOne();
        conn.close();

         */

        //return record;
        return null;
    }

}
