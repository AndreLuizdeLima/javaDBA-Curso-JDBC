import dto.DB;
import exception.DbException;
import exception.DbIntegrityException;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {
    public static void main(String[] args) {

        //Connection conn = DB.getConnetion();
        //DB.closeConnection();

        //selectExemple();

        //insertExemple();

        //updateExemple();

        //deleteExample();

        transactionExample();
    }

    public static void selectExample(){

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

    public static void insertExample(){

        Connection conn = null;
        PreparedStatement st = null;

        //LocalDateTime timeInsert = LocalDateTime.parse("2024-07-20T01:03:26");
        LocalDate dateInsert = LocalDate.now();

        try {
            conn = DB.getConnetion();

            st = conn.prepareStatement(
                    "INSERT INTO seller (Name,Email,BirthDate,BaseSalary, DepartmentId) VALUES (?,?,?,?,?)"
            , Statement.RETURN_GENERATED_KEYS);

            st.setString(1,"Carl Purple");
            st.setString(2,"carl@gmail.com");
            //st.setTimestamp(3, Timestamp.valueOf(timeInsert));  //tempo completo com data e minuto
            st.setDate(3,java.sql.Date.valueOf(dateInsert)); // tempo apenas data
            st.setDouble(4, 3000.0);
            st.setInt(5, 4);

            int rowsAffected  = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                while(rs.next()){
                    int id = rs.getInt(1);
                    System.out.println("Done! id= " + id);
                }
            } else {
                System.out.println("No rown affected!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }


    }

    public static void updateExample(){

        Connection conn = null;
        PreparedStatement st = null;
        try{

            conn = DB.getConnetion();
            st = conn.prepareStatement("UPDATE seller SET BaseSalary = BaseSalary + ? WHERE (DepartmentId = ?)");

            st.setDouble(1, 200.0);
            st.setInt(2, 2);

            int rowsAffected = st.executeUpdate();

            System.out.println("Done! Rows affected: " + rowsAffected);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }



    }

    public static void deleteExample(){
        Connection conn = null;
        PreparedStatement st = null;
        try{

            conn = DB.getConnetion();
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            st.setInt(1, 2);

            int rowsAffected = st.executeUpdate();

            System.out.println("Done! Rows affected: " + rowsAffected);


        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }

    public static void transactionExample(){
        Connection conn = null;
        Statement st = null;
        try{
            conn = DB.getConnetion();
            conn.setAutoCommit(false);
            st = conn.createStatement();

            int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");


            if (false){
                throw new SQLException("Fake error");
            }

            int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");

            conn.commit();

            System.out.println("rows1 " + rows1);
            System.out.println("rows2 " + rows2);

        } catch (SQLException e) {
            try {
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
            }
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }


}