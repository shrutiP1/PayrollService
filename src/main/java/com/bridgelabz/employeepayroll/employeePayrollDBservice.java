package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class employeePayrollDBservice {
    private PreparedStatement employeePayRollDataStatement;
    private static employeePayrollDBservice employeePayrolDBservice;

    private employeePayrollDBservice() {
    }

    public static employeePayrollDBservice getInstance() {
        if (employeePayrolDBservice == null)
            employeePayrolDBservice = new employeePayrollDBservice();
        return employeePayrolDBservice;
    }



    private Connection getConnection() throws employeeDataBaseException {
        // Class.forName("com.mysql.cj.jdbc.Driver");
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "mysql";
        Connection con = null;
        System.out.println("Connecting to database");
        try {
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is sucessful " + con);
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }

        return con;

    }

    public List<EmployeePayRollData> getEmployeePayRollData(String name) throws employeeDataBaseException {
        List<EmployeePayRollData> employeePayRollList = null;
        if (this.employeePayRollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayRollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayRollDataStatement.executeQuery();
            employeePayRollList = this.getEmployeePayRollData(resultSet);

        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return employeePayRollList;
    }

    private List<EmployeePayRollData> getEmployeePayRollData(ResultSet resultSet) throws employeeDataBaseException {
        List<EmployeePayRollData> employeePayRollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayRollList.add(new EmployeePayRollData(id, name, salary, startDate));
            }
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return employeePayRollList;
    }
    public  List<EmployeePayRollData> getEmployeePayForDateRange(LocalDate startDate, LocalDate endDate) throws employeeDataBaseException {
        String sql=String.format("SELECT * FROM emp_payroll WHERE start BETWEEN '%s' AND '%s';",Date.valueOf(startDate),Date.valueOf(endDate));
        return this.getEmployeePayRollDataUsingDB(sql);
    }

    private  List<EmployeePayRollData> getEmployeePayRollDataUsingDB(String sql) throws employeeDataBaseException {
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(sql);
            employeePayRollDataList = this.getEmployeePayRollData(resultset);
        } catch (SQLException | employeeDataBaseException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return employeePayRollDataList;

    }
    public List<EmployeePayRollData> readData() throws employeeDataBaseException {
        String query = "select *from emp_payroll;";
        return this.getEmployeePayRollDataUsingDB(query);
    }


    public int updateEmployeeData(String name, double salary) throws employeeDataBaseException {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) throws employeeDataBaseException {
        String sql = String.format("update emp_payroll set salary=%.2f where name='%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException | employeeDataBaseException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
    }

    private void prepareStatementForEmployeeData() throws employeeDataBaseException {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM emp_payroll WHERE name=?";
            employeePayRollDataStatement = connection.prepareStatement(sql);

        } catch (employeeDataBaseException | SQLException e) {
            throw new employeeDataBaseException("DataBase Problem");
        }
    }


    public int updateEmployeeDataUsingPrepareStatement(String name, double salary) throws employeeDataBaseException {
        try {
            Connection connection = this.getConnection();
            String sql = "update emp_payroll set salary=? where name=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            int count = preparedStatement.executeUpdate();
            return count;
        } catch (SQLException e) {
            throw new employeeDataBaseException("DataBase Problem");
        }


    }


}
