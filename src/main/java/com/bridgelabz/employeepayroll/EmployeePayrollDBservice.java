package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBservice {
    private PreparedStatement employeePayRollDataStatement;
    private static EmployeePayrollDBservice employeePayrolDBservice;

    private EmployeePayrollDBservice() {
    }

    public static EmployeePayrollDBservice getInstance() {
        if (employeePayrolDBservice == null)
            employeePayrolDBservice = new EmployeePayrollDBservice();
        return employeePayrolDBservice;
    }


    private Connection getConnection() throws employeeDataBaseException {
        // Class.forName("com.mysql.cj.jdbc.Driver");
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "mysql";
        Connection con = null;
        System.out.println("Connecting to database");
        try
        {
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

    public List<EmployeePayRollData> getEmployeePayForDateRange(LocalDate startDate, LocalDate endDate) throws employeeDataBaseException {
        String sql = String.format("SELECT * FROM emp_payroll WHERE start BETWEEN '%s' AND '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayRollDataUsingDB(sql);
    }

    private List<EmployeePayRollData> getEmployeePayRollDataUsingDB(String sql) throws employeeDataBaseException {
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

    public Map<String, Double> getAverageSalaryByGender() throws employeeDataBaseException {
        String sql = "SELECT gender,AVG(salary) as Avg_salary from emp_payroll GROUP BY gender;";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection con = this.getConnection()) {
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String gender = result.getString("gender");
                double salary = result.getDouble("Avg_salary");
                genderToAverageSalaryMap.put(gender, salary);
            }
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return genderToAverageSalaryMap;

    }

    public Map<String, Double> getSumOfSalaryByGender() throws employeeDataBaseException {
        String sql = "SELECT gender,SUM(salary) as sum_salary from emp_payroll GROUP BY gender;";
        Map<String, Double> genderToSumOfSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String gender = result.getString("gender");
                double salary = result.getDouble("sum_salary");
                genderToSumOfSalaryMap.put(gender, salary);
            }
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return genderToSumOfSalaryMap;
    }

    public Map<String, Double> getMaxSalaryByGender() throws employeeDataBaseException {
        String sql = "SELECT gender,Max(salary) as max_salary from emp_payroll GROUP BY gender;";
        Map<String, Double> genderToMaxSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String gender = result.getString("gender");
                double salary = result.getDouble("max_salary");
                genderToMaxSalaryMap.put(gender, salary);
            }
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return genderToMaxSalaryMap;
    }

    public Map<String, Double> getMinSalaryByGender() throws employeeDataBaseException {
        String sql = "SELECT gender,Min(salary) as min_salary from emp_payroll GROUP BY gender;";
        Map<String, Double> genderToMinSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String gender = result.getString("gender");
                double salary = result.getDouble("min_salary");
                genderToMinSalaryMap.put(gender, salary);
            }
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return genderToMinSalaryMap;

    }

    public Map<String, Integer> getCountOfEmployyesByGender() throws employeeDataBaseException {
        String sql = "SELECT gender,Count(salary) as count from emp_payroll GROUP BY gender;";
        Map<String, Integer> genderToCountOfEmpMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String gender = result.getString("gender");
                int count = result.getInt("count");
                genderToCountOfEmpMap.put(gender, count);
            }
        } catch (SQLException | employeeDataBaseException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        return genderToCountOfEmpMap;
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

    public EmployeePayRollData addEmployeePayRollUC7(String name, double salary, LocalDate date, String gender) throws employeeDataBaseException {
        int empId=-1;
        EmployeePayRollData employeePayRollData=null;
        String sql=String.format("insert into emp_payroll(name,salary,start,gender) values('%s',%s,'%s','%s')",
                                                                  name,salary,Date.valueOf(date),gender);
        try(Connection connection=this.getConnection())
        {
            Statement statement=connection.createStatement();
            int rowAffected=statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if(rowAffected==1)
            {
                ResultSet resultSet=statement.getGeneratedKeys();
                if(resultSet.next()) empId=resultSet.getInt(1);
            }
            employeePayRollData=new EmployeePayRollData(empId,name,salary,date);
        }
        catch (SQLException throwables)
        {
            throw new employeeDataBaseException("DataBase Exception.");
        }
        return  employeePayRollData;
    }

    public EmployeePayRollData addEmployeePayRoll(String name, double salary, LocalDate date, String gender) throws employeeDataBaseException{
        int empId=-1;
        EmployeePayRollData employeePayRollData=null;
        Connection connection=this.getConnection();
        try(Statement statement=connection.createStatement())
        {
            connection.setAutoCommit(false);
            String sql=String.format("insert into emp_payroll(name,salary,start,gender) values('%s',%s,'%s','%s')",
                    name,salary,Date.valueOf(date),gender);

            int rowAffected=statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if(rowAffected==1)
            {
                ResultSet resultSet=statement.getGeneratedKeys();
                if(resultSet.next()) empId=resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throw new employeeDataBaseException("DataBase Exception");
        }
        try(Statement statement=connection.createStatement())
        {
            double deductions=salary*0.2;
            double taxable_pay=salary-deductions;
            double tax=taxable_pay*0.1;
            double netPay=salary-tax;
            String sql=String.format("insert into payroll_details (employee_id,basic_pay,deductions,taxable_pay,tax,net_Pay)"+
                                         "values(%s,%s,%s,%s,%s,%s)",empId,salary,deductions,taxable_pay,tax,netPay);
            int rowAffected=statement.executeUpdate(sql);
            System.out.println(rowAffected);
            if(rowAffected==1)
                employeePayRollData=new EmployeePayRollData(empId,name,salary,date);

        } catch (SQLException throwables)
        { try
            {
                connection.rollback();
                return employeePayRollData;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return employeePayRollData;
    }
}
