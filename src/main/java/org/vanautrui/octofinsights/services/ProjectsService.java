package org.vanautrui.octofinsights.services;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Optional;

import static org.jooq.impl.DSL.count;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;
import static org.vanautrui.octofinsights.generated.tables.Tasks.TASKS;

public class ProjectsService {

    public static Result<Record> getProjectsByUserId(int user_id)throws Exception{

        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        Result<Record> myprojects = ctx.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.USER_ID.eq(user_id)).fetch();

        conn.close();

        return myprojects;
    }

    public static int getCompletionPercentage(int project_id,int user_id){
        //add up the tasks , their effort estimate (for now, because we do not track real efforts right now)
        Connection conn;
        try {
            conn = DBUtils.makeDBConnection();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        Optional<Record1<BigDecimal>> result1 = ctx
                .select(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS.sum())
                .from(TASKS)
                .where(TASKS.PROJECT_ID.eq(project_id).and(TASKS.USER_ID.eq(user_id)))
                .fetchOptional();

        if(result1==null || !result1.isPresent()){
            return 0;
        }

        BigDecimal total_project_estimate_hours=result1.get().component1();

        //add up the  completed tasks , their effort estimate (for now, because we do not track real efforts right now)
        Optional<Record1<BigDecimal>> result2 = ctx
                .select(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS.sum())
                .from(TASKS)
                .where(TASKS.PROJECT_ID.eq(project_id).and(TASKS.USER_ID.eq(user_id)).and(TASKS.ISCOMPLETED.eq((byte)1)))
                .fetchOptional();

        if(result2==null || !result2.isPresent() || result2.get().component1()==null){
            return 0;
        }

        BigDecimal total_project_estimate_hours_completed=result2.get().component1();

        //System.out.println(total_project_estimate_hours);
        //System.out.println(total_project_estimate_hours_completed);

        double total = total_project_estimate_hours.doubleValue();
        double completed = total_project_estimate_hours_completed.doubleValue();

        double ratio = completed/total;
        //System.out.println("ratio: "+ratio);
        return (int)(ratio*100);
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

    public static Record getById(int user_id, int project_id) throws Exception{

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Record record = create.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).fetchOne();
        conn.close();

        return record;
    }

    public static void updateProject(int user_id, int id, Optional<String> project_name, Optional<String> project_description) throws Exception{

        //https://www.jooq.org/doc/3.12/manual/sql-building/sql-statements/update-statement/

        System.out.println(project_name);
        System.out.println(project_description);

        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        if(project_name.isPresent() && project_description.isPresent()) {

            ctx.update(PROJECTS)
                    .set(PROJECTS.PROJECT_NAME, project_name.get())
                    .set(PROJECTS.PROJECT_DESCRIPTION, project_description.get())
                    .where(PROJECTS.ID.eq(id).and(PROJECTS.USER_ID.eq(user_id)))
                    .execute();

        }else if(project_description.isPresent()){

            ctx.update(PROJECTS)
                    .set(PROJECTS.PROJECT_DESCRIPTION, project_description.get())
                    .where(PROJECTS.ID.eq(id).and(PROJECTS.USER_ID.eq(user_id)))
                    .execute();

        }else if(project_name.isPresent()){

            ctx.update(PROJECTS)
                    .set(PROJECTS.PROJECT_NAME, project_name.get())
                    .where(PROJECTS.ID.eq(id).and(PROJECTS.USER_ID.eq(user_id)))
                    .execute();

        }

        conn.close();
    }

    public static long getActiveProjectsCount(int user_id) throws Exception{
        //https://www.youtube.com/watch?v=BqtiQEZA1Z8

        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Record1<Integer> count = create.select(count(PROJECTS)).from(PROJECTS).where(PROJECTS.USER_ID.eq(user_id).and(PROJECTS.ISACTIVE.eq((byte)1))).fetchOne();
        conn.close();
        return count.component1().longValue();
    }
}
