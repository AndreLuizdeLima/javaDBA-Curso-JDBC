package application;

import db.Db;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        SellerDao sellerdao = DaoFactory.createSellerDao();
        Seller seller = sellerdao.findById(3);

        System.out.println(seller);
    }
}