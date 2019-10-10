package org.vanautrui.octofinsights.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.vanautrui.octofinsights.generated.tables.Leads.LEADS;

public class LeadsService {

    public static List<Record> getLeads(int user_id, Optional<String> searchQuery) throws Exception{
        try(Connection conn= DBUtils.makeDBConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node = mapper.createArrayNode();

            //Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).fetch().sortAsc(LEADS.LEAD_STATUS.startsWith("open"));
            Result<Record> records = create.select(LEADS.asterisk()).from(LEADS).where(LEADS.USER_ID.eq(user_id)).fetch().sortDesc(LEADS.LEAD_STATUS, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.startsWith("open")) {
                        return 1;
                    }
                    if (o2.startsWith("open")) {
                        return -1;
                    }
                    return 0;
                }
            });

            //return records;

            List<Record> filtered_records;
            if (searchQuery.isPresent()) {
                final String my_query = searchQuery.get().toLowerCase();
                filtered_records = records
                        .stream()
                        .filter(record -> record.getValue(LEADS.LEAD_NAME).toLowerCase().contains(my_query) || record.getValue(LEADS.WHAT_THE_LEAD_WANTS).toLowerCase().contains(my_query))
                        .collect(Collectors.toList());
            } else {
                filtered_records = records;
            }
            return filtered_records;
        }
    }

    public static List<Record> getOpenLeads(int user_id)throws Exception{

        return getLeads(user_id,Optional.empty()).stream().filter(lead->lead.get(LEADS.LEAD_STATUS).startsWith("open")).collect(Collectors.toList());
    }

    public static int getOpenLeadsCount(int user_id) throws Exception{
        return getOpenLeads(user_id).size();
    }
}
