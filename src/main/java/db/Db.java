package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Db {

    private static Connection conn = null;

    public static Connection getConnetion(){
        if (conn == null){

            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                String user = props.getProperty("user");
                String password = props.getProperty("password");
                conn = DriverManager.getConnection(url,user,password);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection(){
        if(conn != null){
            try{
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement st){
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSte(ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties(){
        try(InputStream fs =  Db.class.getClassLoader().getResourceAsStream("db.properties")){
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e){
            throw new DbException(e.getMessage());
        }

    }

    public static class DbException extends RuntimeException {

        public DbException(String message) {
            super(message);
        }
    }

    public static class DbIntegrityException extends RuntimeException {
        public DbIntegrityException(String message) {
            super(message);
        }
    }
}
