package application;

import db.Db;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        SellerDao sellerdao = DaoFactory.createSellerDao();
        Seller seller = sellerdao.findById(3);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = departmentDao.findById(2);
        System.out.println(seller);
        System.out.println(department + "\n");

        List<Department> list = departmentDao.findAll();
        List<Seller> sellerList = sellerdao.findAll();

        list.forEach(System.out::println);
        sellerList.forEach(System.out::println);
    }
}