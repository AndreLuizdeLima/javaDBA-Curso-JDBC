package model.dao.impl;

import db.Db;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Department obj) {
    }

    @Override
    public void update(Department obj) {

    }

    @Override
    public void deleteByID(Integer id) {

    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("select Id, Name from department where Id = ?");
            st.setInt(1,id);
            rs = st.executeQuery();
            if (rs.next()){
                Department dep = new Department();
                dep.setId(rs.getInt("Id"));
                dep.setName(rs.getString("Name"));
                return dep;
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
    public List<Department> findAll() {

        Statement st = null;
        ResultSet rs = null;

        List<Department> departmentList = new ArrayList<>();
        try {

            st = conn.createStatement();
            rs = st.executeQuery("select Id , Name from department");

            while (rs.next()){
                Department dep = new Department();
                dep.setId(rs.getInt("Id"));
                dep.setName(rs.getString("Name"));
                departmentList.add(dep);
            }

            return departmentList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            Db.closeResultSte(rs);
            Db.closeStatement(st);
        }


    }
}
