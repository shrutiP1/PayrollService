package com.bridgelabz.employeepayroll;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeeService {
    private static EmployeePayrollDBservice employeePayrolDBservice;
    List<EmployeePayRollData> employeePayRollList;

    public enum ioService {
        CONOSLE_IO, FILE_IO, DB_IO
    }

    public EmployeeService()
    {
        employeePayrolDBservice= EmployeePayrollDBservice.getInstance();
    }

    public EmployeeService(List<EmployeePayRollData> employeePayRollList)
    {
        this();
        this.employeePayRollList = employeePayRollList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayRollData> employeePayRollList = new ArrayList<>();
        EmployeeService employeeService = new EmployeeService(employeePayRollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeeService.readEmpPayRollData(consoleInputReader);
        employeeService.writeEmpPayRollData(ioService.FILE_IO);

    }

    private void readEmpPayRollData(Scanner consoleInputReader) {
        System.out.println("Enter id");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Name");
        String name = consoleInputReader.next();
        System.out.println("Enter Salary");
        double salary = consoleInputReader.nextDouble();
        employeePayRollList.add(new EmployeePayRollData(id, name, salary));
    }

    public void writeEmpPayRollData(ioService ioService) {
        if (ioService.equals(ioService.CONOSLE_IO))
            System.out.println(employeePayRollList);
        else if (ioService.equals(ioService.FILE_IO))
            EmployeePayrollFileIOService.writeData(employeePayRollList);
    }

    public void printData(ioService ioService) {
        if (ioService.equals(EmployeeService.ioService.FILE_IO)) {
            EmployeePayrollFileIOService.printData();
        }
    }

    public long countEntries(ioService ioService) {
        if (ioService.equals(ioService.FILE_IO)) {
            return EmployeePayrollFileIOService.countEntries();
        } else {
            return employeePayRollList.size();
        }
    }

    public long readData(ioService ioservice) {
        List<EmployeePayRollData> list = new ArrayList<>();
        if (ioservice.equals(ioService.FILE_IO))
            list = EmployeePayrollFileIOService.readData();
        System.out.println(list);
        return list.size();
    }

    public List<EmployeePayRollData> readEmployeePayrollData(ioService ioService) throws employeeDataBaseException {
        if (ioService.equals(ioService.DB_IO)) {
            this.employeePayRollList = employeePayrolDBservice.readData();
            return employeePayRollList;
        }
        return null;
    }
    public static List<EmployeePayRollData> readEmployeePayRollDataRange(ioService ioService, LocalDate startDate, LocalDate endDate) throws employeeDataBaseException {
             if(ioService.equals(ioService.DB_IO))
                 return employeePayrolDBservice.getEmployeePayForDateRange(startDate,endDate);
             return null;
    }
    public Map<String, Double> readAverageSalaryByGender(ioService ioService) throws employeeDataBaseException {
        if(ioService.equals(ioService.DB_IO))
            return employeePayrolDBservice.getAverageSalaryByGender();
        return null;
    }
    public Map<String, Double> readSumOfSalaryNyGender(ioService ioService) throws employeeDataBaseException {
        if(ioService.equals(ioService.DB_IO))
            return  employeePayrolDBservice.getSumOfSalaryByGender();
        return null;
    }
    public Map<String, Double> readMaxSalaryNyGender(ioService ioService) throws employeeDataBaseException {
        if(ioService.equals(ioService.DB_IO))
            return  employeePayrolDBservice.getMaxSalaryByGender();
        return null;
    }
    public Map<String, Double> readMinSalaryNyGender(ioService ioService) throws employeeDataBaseException {
        if(ioService.equals(ioService.DB_IO))
            return  employeePayrolDBservice.getMinSalaryByGender();
        return null;
    }
    public Map<String, Integer> readCountOfEmployeeByGender(ioService ioService) throws employeeDataBaseException {
        if(ioService.equals(ioService.DB_IO))
            return  employeePayrolDBservice.getCountOfEmployyesByGender();
        return null;
    }

    public void updateEmployeeSalary(String name, double salary) throws employeeDataBaseException {
        int result=employeePayrolDBservice.updateEmployeeData(name,salary);
        if(result==0)
            throw new employeeDataBaseException("Database Exception");
        EmployeePayRollData employeePayRollData=this.getEmpPayrollData(name);
        if(employeePayRollData!=null) employeePayRollData.salary=salary;
    }
    public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary) throws employeeDataBaseException {
        int result=employeePayrolDBservice.updateEmployeeDataUsingPrepareStatement(name,salary);
        if(result==0)
            throw new employeeDataBaseException("Database Exception");
        EmployeePayRollData employeePayRollData=this.getEmpPayrollData(name);
        if(employeePayRollData!=null) employeePayRollData.salary=salary;
    }
    public void addEmployeeToPayRoll(String name, double salary, LocalDate date, String gender) throws employeeDataBaseException, SQLException {
        employeePayRollList.add(employeePayrolDBservice.addEmployeePayRoll(name,salary,date,gender));
    }
    public  void addEmployeeToPayRoll(List<EmployeePayRollData> employeePayRollDataList) 
    {
        employeePayRollDataList.stream().forEach(employeePayRollData ->
                {
                    try {
                        System.out.println("Employee Being added "+employeePayRollData.name);
                        this.addEmployeeToPayRoll(employeePayRollData.name,employeePayRollData.salary,employeePayRollData.startDate,employeePayRollData.gender);
                        System.out.println("Employee Added "+employeePayRollData.name);
                    }
                    catch (SQLException | employeeDataBaseException throwables) {
                       
                    }
                    
                }
                );
        System.out.println(employeePayRollDataList);
    }

    private EmployeePayRollData getEmpPayrollData(String name)
    {
        return employeePayRollList.stream()
                                  .filter(employeePayrollDataItem->employeePayrollDataItem.name.equals(name))
                                  .findFirst()
                                  .orElse(null);
    }
    public boolean checkEmployeePayRollInSyncWithDB(String name) throws employeeDataBaseException {
        List<EmployeePayRollData> employeePayRollDataList= EmployeePayrollDBservice.getInstance().getEmployeePayRollData(name);
        return employeePayRollDataList.get(0).equals(getEmpPayrollData(name));

    }


}
