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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("Insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId)" +
                    " values (?,?,?,?,?)" , Statement.RETURN_GENERATED_KEYS);
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, Date.valueOf(seller.getBirthDate()));
            st.setDouble(4,seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
                Db.closeResultSte(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            Db.closeStatement(st);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, " +
                    "BaseSalary = ?, DepartmentId = ? WHERE Id = ?" );
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, Date.valueOf(seller.getBirthDate()));
            st.setDouble(4,seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6,seller.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            Db.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("delete from seller where Id = ?");
            st.setInt(1,id);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0){
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            Db.closeStatement(st);
        }
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
                Department dep = instantiateDepartment(rs);
                return instantiateSeller(rs,dep);
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

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate").toLocalDate());
        obj.setDepartment(dep);
        return obj;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;


        try {

            st = conn.prepareStatement("select seller.*, department.Name as DepName from seller " +
                    "inner join department on seller.DepartmentId = department.Id order by Name");

            rs = st.executeQuery();
            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                sellerList.add(instantiateSeller(rs,dep));
            }

            return sellerList;

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            Db.closeResultSte(rs);
            Db.closeStatement(st);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("select seller.*, department.Name as DepName from seller " +
                    "inner join department on seller.DepartmentId = department.Id where DepartmentId = ? order by Name");

            st.setInt(1, department.getId());
            rs = st.executeQuery();
            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                sellerList.add(instantiateSeller(rs,dep));
            }
            return sellerList;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            Db.closeStatement(st);
            Db.closeResultSte(rs);
        }
    }
}
