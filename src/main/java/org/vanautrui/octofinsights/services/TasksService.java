package org.vanautrui.octofinsights.services;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;

import static org.vanautrui.octofinsights.generated.tables.Tasks.TASKS;

public class TasksService {

    public static int countTasksByUserId(int user_id)throws Exception{

        //this method is for the widgets, to show the user that tasks are to be completed

        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        int count = ctx.select(TASKS.asterisk()).from(TASKS).where(TASKS.USER_ID.eq(user_id)).fetchCount();

        conn.close();

        return count;
    }

    public static Result<Record> getTasksByUserIdAndProjectId(int user_id,int project_id)throws Exception{

        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        Result<Record> mytasks = ctx.select(TASKS.asterisk()).from(TASKS).where(TASKS.USER_ID.eq(user_id).and(TASKS.PROJECT_ID.eq(project_id))).fetch();

        conn.close();

        return mytasks;
    }

    public static void insertTask(
            int user_id,
            int project_id,
            String task_name,
            int effort_estimate
    )throws Exception{

        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);
        ctx.insertInto(TASKS)
                .columns(
                        TASKS.USER_ID,
                        TASKS.PROJECT_ID,
                        TASKS.TASK_NAME,
                        TASKS.INITIAL_EFFORT_ESTIMATE_HOURS,
                        TASKS.EFFORT_SPENT
                )
                .values(
                        user_id,
                        project_id,
                        task_name,
                        effort_estimate,
                        0
                )
                .execute();
        conn.close();
    }

    public static void completeTask(int id, int user_id) throws Exception{
        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        ctx.update(TASKS)
                .set(TASKS.ISCOMPLETED,(byte)1)
                .where(TASKS.ID.eq(id).and(TASKS.USER_ID.eq(user_id)))
                .execute();

        conn.close();
    }

    public static void uncompleteTask(int id, int user_id) throws Exception{
        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        ctx.update(TASKS)
                .set(TASKS.ISCOMPLETED,(byte)0)
                .where(TASKS.ID.eq(id).and(TASKS.USER_ID.eq(user_id)))
                .execute();

        conn.close();
    }

    public static void deleteTask(int id, int user_id) throws Exception{
        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        ctx.delete(TASKS)
                .where(TASKS.ID.eq(id).and(TASKS.USER_ID.eq(user_id)))
                .execute();

        conn.close();
    }

    public static void spend1hour(int id, int user_id) throws Exception{
        Connection conn = DBUtils.makeDBConnection();
        DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

        ctx.update(TASKS)
                .set(TASKS.EFFORT_SPENT,TASKS.EFFORT_SPENT.add(1))
                .where(TASKS.ID.eq(id).and(TASKS.USER_ID.eq(user_id)))
                .execute();

        conn.close();
    }
}
