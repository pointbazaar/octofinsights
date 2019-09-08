package org.vanautrui.octofinsights.services;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;

import static org.vanautrui.octofinsights.generated.Tables.USERS;

public class UsersService {

  public static Record getUserById(int user_id) throws Exception{
    Connection conn= DBUtils.makeDBConnection();
    DSLContext ctx = DSL.using(conn, SQLDialect.MYSQL);

    Record user = ctx.select(USERS.asterisk()).from(USERS).where(USERS.ID.eq(user_id)).fetchOne();

    ctx.close();
    return user;
  }
}
