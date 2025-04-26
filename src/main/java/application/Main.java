package application;

import db.Db;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        SellerDao sellerdao = DaoFactory.createSellerDao();
//        System.out.println("=== Test Seller Find By Id");
//        Seller seller = sellerdao.findById(3);
//        System.out.println(seller);
//
//        System.out.println("=== Test Seller Find By Department");
//        List<Seller> sellerList = sellerdao.findAll();
//        sellerList.forEach(System.out::println);
//
//        Seller newSeller = new Seller(null, "Andre", "andre@gmail.com", LocalDate.now(), 2000.0, new Department(null,"Novo"));
//        sellerdao.insert(newSeller);
//        System.out.println("Inserted New id " + newSeller.getId());
//
//        Seller seller2 = sellerdao.findById(1);
//        System.out.println(seller2);
//        seller2.setName("Bob Brown update");
//        sellerdao.update(seller2);
//        System.out.println(seller2);

        sellerdao.deleteByID(9);


    }
}