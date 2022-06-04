package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PGLogEntry {
    private Date log_time;
    private String user_name;
    private String database_name;
    private Integer process_id;
    private String connection_from;
    private String session_id;
    private Long session_line_num;
    private String command_tag;
    private Date session_start_time;
    private String virtual_transaction_id;
    private Long transaction_id;
    private String error_severity;
    private String sql_state_code;
    private String message;
    private String detail;
    private String hint;
    private String internal_query;
    private Integer internal_query_pos;
    private String context;
    private String query;
    private Integer query_pos;
    private String location;
    private String application_name;
    private String backend_type;
    private Integer leader_pid;
    private Long query_id;

    public PGLogEntry(String[] attributes) {
        log_time = getDateFromStringMillisecond(attributes[0]);
        user_name = attributes[1];
        database_name = attributes[2];
        try {
            process_id = Integer.parseInt(attributes[3]);
        } catch (NumberFormatException e) {
            process_id = null;
        }

        connection_from = attributes[4];
        session_id = attributes[5];
        try {
            session_line_num = Long.parseLong(attributes[6]);
        } catch (NumberFormatException e) {
            session_line_num = null;
        }

        command_tag = attributes[7];
        session_start_time = getDateFromString(attributes[8]);
        virtual_transaction_id = attributes[9];
        try {
            transaction_id = Long.parseLong(attributes[10]);
        } catch (NumberFormatException e) {
            transaction_id = null;
        }

        error_severity = attributes[11];
        sql_state_code = attributes[12];
        message = attributes[13];
        detail = attributes[14];
        hint = attributes[15];
        internal_query = attributes[16];
        try {
            internal_query_pos = Integer.parseInt(attributes[17]);
        } catch (NumberFormatException e) {
            internal_query_pos = null;
        }

        context = attributes[18];
        query = attributes[19];
        try {
            query_pos = Integer.parseInt(attributes[21]);
        } catch (NumberFormatException e) {
            query_pos = null;
        }

        location = attributes[21];
        application_name = attributes[22];
        backend_type = attributes[23];
        try {
            leader_pid = Integer.parseInt(attributes[24]);
        } catch (NumberFormatException e) {
            leader_pid = null;
        }

        try {
            query_id = Long.parseLong(attributes[25]);
        } catch (NumberFormatException e) {
            query_id = null;
        }

    }

    public String getBackend_type() {
        return backend_type;
    }

    public Integer getLeader_pid() {
        return leader_pid;
    }

    public Long getQuery_id() {
        return query_id;
    }

    public Date getLog_time() {
        return log_time;
    }

    public Integer getProcess_id() {
        return process_id;
    }

    public Long getSession_line_num() {
        return session_line_num;
    }

    public String getCommand_tag() {
        return command_tag;
    }

    public Date getSession_start_time() {
        return session_start_time;
    }

    public String getVirtual_transaction_id() {
        return virtual_transaction_id;
    }

    public Long getTransaction_id() {
        return transaction_id;
    }

    public String getError_severity() {
        return error_severity;
    }

    public String getSql_state_code() {
        return sql_state_code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public String getHint() {
        return hint;
    }

    public String getInternal_query() {
        return internal_query;
    }

    public Integer getInternal_query_pos() {
        return internal_query_pos;
    }

    public String getContext() {
        return context;
    }

    public String getQuery() {
        return query;
    }

    public Integer getQuery_pos() {
        return query_pos;
    }

    public String getLocation() {
        return location;
    }

    public String getConnection_from() {
        return connection_from;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getApplication_name() {
        return application_name;
    }

    private Date getDateFromStringMillisecond(String timestamp) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS z";
        try {
            Date date = new SimpleDateFormat(dateFormat).parse(timestamp);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date getDateFromString(String timestamp) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss z";
        try {
            Date date = new SimpleDateFormat(dateFormat).parse(timestamp);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
