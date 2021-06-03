package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class employeePayrollDBservice
{
    private Connection getConnection() throws employeeDataBaseException {
       // Class.forName("com.mysql.cj.jdbc.Driver");
        String jdbcURL="jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName="root";
        String password="mysql";
        Connection con = null;
        System.out.println("Connecting to database");
        try
        {
            con= DriverManager.getConnection(jdbcURL,userName,password);
            System.out.println("Connection is sucessful "+con );
        } catch (SQLException throwables)
        {
            throw new employeeDataBaseException("DataBase Exception");
        }

        return con;

    }

    public List<EmployeePayRollData> readData() throws employeeDataBaseException {
        String query="select *from emp_payroll;";
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        try(Connection connection=this.getConnection())
        {

            Statement statement=connection.createStatement();
            ResultSet resultset=statement.executeQuery(query);
           while(resultset.next())
           {
               int id=resultset.getInt("id");
               String name=resultset.getString("name");
               Double salary=resultset.getDouble("salary");
               LocalDate startDate=resultset.getDate("start").toLocalDate();
               employeePayRollDataList.add(new EmployeePayRollData(id,name,salary,startDate));
           }
        }
        catch (SQLException throwables)
        {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return employeePayRollDataList;

    }


}
