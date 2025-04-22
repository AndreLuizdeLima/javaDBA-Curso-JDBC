package application;

import db.Db;
import model.entities.Department;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        Connection conn = Db.getConnetion();
        Db.closeConnection();


    }
}