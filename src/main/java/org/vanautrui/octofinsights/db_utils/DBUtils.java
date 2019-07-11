package org.vanautrui.octofinsights.db_utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class DBUtils {


    public static Connection makeDBConnection() throws Exception{
        List<String> credentials = Files.readAllLines(Paths.get("credentials.txt"));

        String userName = credentials.get(0);
        String password = credentials.get(1);



        String url = "jdbc:mysql://vanautrui.org:3306/octofinsights";

        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        Connection conn = DriverManager.getConnection(url, userName, password);


        //Result<Record> result = create.select().from(SALES).fetch();

        return conn;
    }
}
