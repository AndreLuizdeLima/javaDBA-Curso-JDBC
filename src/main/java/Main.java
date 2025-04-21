import dto.DB;
import exception.DbException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        //Connection conn = DB.getConnetion();
        //DB.closeConnection();

        selectExemple();
    }

    public static void selectExemple(){

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            conn = DB.getConnetion();
            st = conn.createStatement();
            rs = st.executeQuery("select * from department");

            while (rs.next()){
                System.out.println(rs.getInt("Id") + " , " + rs.getString("Name"));
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSte(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }


    }
}