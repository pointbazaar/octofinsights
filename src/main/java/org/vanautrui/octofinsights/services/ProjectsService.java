package org.vanautrui.octofinsights.services;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.generated.tables.Customers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import static org.jooq.impl.DSL.count;
import static org.vanautrui.octofinsights.generated.tables.Customers.CUSTOMERS;
import static org.vanautrui.octofinsights.generated.tables.Projects.PROJECTS;
import static org.vanautrui.octofinsights.generated.tables.Tasks.TASKS;

public final class ProjectsService {

    public static Result<Record> getProjectsByUserId(int user_id)throws Exception{

        try(Connection conn = DBUtils.makeDBConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            return ctx.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.USER_ID.eq(user_id)).fetch();
        }
    }

    public static int getCompletionPercentage(int project_id,int user_id)throws Exception{
        //add up the tasks , their effort estimate (for now, because we do not track real efforts right now)
        try(Connection conn = DBUtils.makeDBConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
            Optional<Record1<BigDecimal>> result1 = ctx
                    .select(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS.sum())
                    .from(TASKS)
                    .where(TASKS.PROJECT_ID.eq(project_id).and(TASKS.USER_ID.eq(user_id)))
                    .fetchOptional();

            if (result1 == null || !result1.isPresent()) {
                return 0;
            }

            BigDecimal total_project_estimate_hours = result1.get().component1();

            //add up the  completed tasks , their effort estimate (for now, because we do not track real efforts right now)
            Optional<Record1<BigDecimal>> result2 = ctx
                    .select(TASKS.INITIAL_EFFORT_ESTIMATE_HOURS.sum())
                    .from(TASKS)
                    .where(TASKS.PROJECT_ID.eq(project_id).and(TASKS.USER_ID.eq(user_id)).and(TASKS.ISCOMPLETED.eq((byte) 1)))
                    .fetchOptional();

            if (result2 == null || !result2.isPresent() || result2.get().component1() == null) {
                return 0;
            }

            BigDecimal total_project_estimate_hours_completed = result2.get().component1();

            //System.out.println(total_project_estimate_hours);
            //System.out.println(total_project_estimate_hours_completed);

            double total = total_project_estimate_hours.doubleValue();
            double completed = total_project_estimate_hours_completed.doubleValue();

            double ratio = completed / total;
            //System.out.println("ratio: "+ratio);
            return (int) (ratio * 100);
        }
    }

    public static void insertProject(
            int user_id,
            String project_name, Timestamp project_start,
            Timestamp project_end_estimate,
            /*Timestamp project_end,*/
            int initial_effort_estimate_hours,
            int project_earnings_estimate,
            String project_description,
            int customer_id
    )throws Exception{
        byte isActive=1;
        try(Connection conn = DBUtils.makeDBConnection()) {
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
                            PROJECTS.ISACTIVE,
                            PROJECTS.CUSTOMER_ID
                    )
                    .values(
                            user_id,
                            project_name,
                            project_description,
                            project_start,
                            project_end_estimate,
                            initial_effort_estimate_hours,
                            project_earnings_estimate,
                            isActive,
                            customer_id
                    )
                    .execute();
        }
    }

    public static Record getById(int user_id, int project_id) throws Exception{

        try(Connection conn= DBUtils.makeDBConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            return create.select(PROJECTS.asterisk()).from(PROJECTS).where(PROJECTS.ID.eq(project_id).and(PROJECTS.USER_ID.eq(user_id))).fetchOne();
        }
    }

    public static void updateProject(
            final int id,
            final int user_id,
            final int customer_id,
            final String project_name,
            final Date new_end_date,
            final String project_description

    ) throws Exception{

        //https://www.jooq.org/doc/3.12/manual/sql-building/sql-statements/update-statement/

        System.out.println(project_name);
        System.out.println(project_description);

        try(Connection conn = DBUtils.makeDBConnection()) {
            final DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

            ctx
                    .update(PROJECTS)

                    .set(PROJECTS.CUSTOMER_ID, customer_id)
                    .set(PROJECTS.PROJECT_NAME, project_name)
                    .set(PROJECTS.PROJECT_DESCRIPTION, project_description)
                    .set(PROJECTS.PROJECT_END,new Timestamp(new_end_date.getTime()))

                    //checking  if the project even belongs to this user_id
                    //checking that the new customer thats being assigns belongs to this user_id

                    .where(
                            PROJECTS.ID.eq(id)
                            .and(PROJECTS.USER_ID.eq(user_id))
                            .and(PROJECTS.CUSTOMER_ID.in(
                                ctx.select(CUSTOMERS.ID).from(CUSTOMERS).where(CUSTOMERS.USER_ID.eq(user_id))
                            ))
                    )
                    .execute();
        }
    }

    public static long getActiveProjectsCount(int user_id) throws Exception{
        //https://www.youtube.com/watch?v=BqtiQEZA1Z8

        try(Connection conn= DBUtils.makeDBConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            return create.select(count(PROJECTS)).from(PROJECTS).where(PROJECTS.USER_ID.eq(user_id).and(PROJECTS.ISACTIVE.eq((byte) 1))).fetchOne().component1().longValue();
        }
    }

}
