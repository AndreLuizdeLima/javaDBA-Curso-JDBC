package model.dao.impl;

import db.Db;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteByID(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("select seller.*, department.Name as DepName from seller " +
                    "inner join department on seller.DepartmentId = department.Id " +
                    "where seller.Id = ? ");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()){
                Department dep = new Department();
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));
                Seller obj = new Seller();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                obj.setEmail(rs.getString("Email"));
                obj.setBaseSalary(rs.getDouble("BaseSalary"));
                obj.setBirthDate(rs.getDate("BirthDate").toLocalDate());
                obj.setDepartment(dep);
                return obj;
            } else {
                return null;
            }
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            Db.closeStatement(st);
            Db.closeResultSte(rs);
        }


    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        List<Seller> sellerList = new ArrayList<>();
        try {

            st = conn.prepareStatement("select seller.*, department.Name as DepName from seller " +
                    "inner join department on seller.DepartmentId = department.Id");

            rs = st.executeQuery();

            while(rs.next()){
                Department dep = new Department();
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));
                Seller sell = new Seller();
                sell.setId(rs.getInt("Id"));
                sell.setName(rs.getString("Name"));
                sell.setEmail(rs.getString("Email"));
                sell.setBirthDate(rs.getDate("BirthDate").toLocalDate());
                sell.setBaseSalary(rs.getDouble("BaseSalary"));
                sell.setDepartment(dep);

                sellerList.add(sell);
            }

            return sellerList;

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            Db.closeResultSte(rs);
            Db.closeStatement(st);
        }

    }
}
