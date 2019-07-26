package org.vanautrui.octofinsights.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.generated.tables.Expenses;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.YearMonth;

import static org.vanautrui.octofinsights.generated.Tables.EXPENSES;
import static org.vanautrui.octofinsights.generated.Tables.USERS;

public class ProfileService {

    public static String getEmailById(int user_id) throws Exception{
        Connection conn= DBUtils.makeDBConnection();
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Record1<String> record = create.select(USERS.EMAIL).from(USERS).where(USERS.ID.eq(user_id)).fetchOne();

        return record.getValue(USERS.EMAIL);
    }
}
