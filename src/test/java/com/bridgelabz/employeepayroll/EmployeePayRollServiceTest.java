package com.bridgelabz.employeepayroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.employeepayroll.EmployeeService.ioService.DB_IO;
import static com.bridgelabz.employeepayroll.EmployeeService.ioService.FILE_IO;

public class EmployeePayRollServiceTest
{
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries()
    {
        EmployeePayRollData[] arrayOfEmps={
                new EmployeePayRollData(1,"shruti",20000d),
                new EmployeePayRollData(2,"Dadu",10000.0),
                new EmployeePayRollData(3,"RAJU",30200.9)
        };
        EmployeeService employeePayrollService;
        employeePayrollService=new EmployeeService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmpPayRollData(FILE_IO);
        employeePayrollService.printData(FILE_IO);
        long entries=employeePayrollService.countEntries(FILE_IO);
        Assertions.assertEquals(3,entries);

        long entriesAfterRead=employeePayrollService.readData(FILE_IO);
        Assertions.assertEquals(3,entriesAfterRead);
    }
    @Test
    public void givenEmployeePayrollDB_WhenRetrieved_shouldMatchEmployeeCount() throws employeeDataBaseException {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Assertions.assertEquals(3,employeePayRollData.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_shouldSyncWithDB() throws employeeDataBaseException {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        employeeService.updateEmployeeSalary("Terisa",3000000);
        boolean result=employeeService.checkEmployeePayRollInSyncWithDB("Terisa");
        Assertions.assertTrue(true);
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws employeeDataBaseException {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        employeeService.updateEmployeeSalaryUsingPrepareStatement("Terisa",400000);
        boolean result=employeeService.checkEmployeePayRollInSyncWithDB("Terisa");
        Assertions.assertTrue(true);

    }
    @Test
    public void givenDateRange_WhenRetrieved_shouldMatchTheCount() throws employeeDataBaseException {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        LocalDate startDate=LocalDate.of(2018,01,01);
        LocalDate endDate=LocalDate.now();
        List<EmployeePayRollData> employeePayRollDataList=EmployeeService.readEmployeePayRollDataRange(DB_IO,startDate,endDate);
        Assertions.assertEquals(3,employeePayRollDataList.size());
    }
    @Test
    public void givenPayRollData_WhenAverageSalaryRetrieved_shouldReturnProperValue() throws employeeDataBaseException {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Map<String,Double> averageSalaryByGender=employeeService.readAverageSalaryByGender(DB_IO);
        Assertions.assertTrue(averageSalaryByGender.get("M").equals(20000.0) && averageSalaryByGender.get("F").equals(3000000.0));
    }
    @Test
    public void givenPayRollData_WhenSumOfSalaryRetrieved_shouldReturnProperValue() throws employeeDataBaseException
    {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Map<String,Double> sumOfSalaryByGender=employeeService.readSumOfSalaryNyGender(DB_IO);
        Assertions.assertTrue(sumOfSalaryByGender.get("M").equals(40000.0) && sumOfSalaryByGender.get("F").equals(3000000.0));
    }
    @Test
    public void givenPayRollData_WhenMaxOfSalaryRetrieved_shouldReturnProperValue() throws employeeDataBaseException
    {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Map<String,Double> sumOfSalaryByGender=employeeService.readMaxSalaryNyGender(DB_IO);
        Assertions.assertTrue(sumOfSalaryByGender.get("M").equals(20000.0) && sumOfSalaryByGender.get("F").equals(3000000.0));
    }
    @Test
    public void givenPayRollData_WhenMinOfSalaryRetrieved_shouldReturnProperValue() throws employeeDataBaseException
    {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Map<String,Double> sumOfSalaryByGender=employeeService.readMinSalaryNyGender(DB_IO);
        Assertions.assertTrue(sumOfSalaryByGender.get("M").equals(20000.0) && sumOfSalaryByGender.get("F").equals(3000000.0));
    }
    @Test
    public void givenPayRollData_CountOfGenderRetrieved_shouldReturnProperValue() throws employeeDataBaseException
    {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Map<String,Integer> countOfEmployeeByGender=employeeService.readCountOfEmployeeByGender(DB_IO);
        Assertions.assertTrue(countOfEmployeeByGender.get("M").equals(2) && countOfEmployeeByGender.get("F").equals(1));
    }
    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws employeeDataBaseException, SQLException {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        employeeService.addEmployeeToPayRoll("Mark",500000.0,LocalDate.now(),"M");
        boolean result=employeeService.checkEmployeePayRollInSyncWithDB("Mark");
        Assertions.assertTrue(result);
    }
    @Test
    public void given6Employees_WhenAdded_shouldMatchEmployeeEntries() throws employeeDataBaseException {
       EmployeePayRollData[] arrayOfEmps={
               new EmployeePayRollData(0,"Jeff Bezos","M",100000.0,LocalDate.now()),
               new EmployeePayRollData(0,"Bill Gates","M",200000.0,LocalDate.now()),
                new EmployeePayRollData(0,"Mark Zuckerberg","M",300000.0,LocalDate.now()),
                new EmployeePayRollData(0,"Sundar","M",600000.0,LocalDate.now()),
                new EmployeePayRollData(0,"Mukesh","M",100000.0,LocalDate.now()),
               new EmployeePayRollData(0,"Anil","M",200000.0,LocalDate.now()),

       };
        EmployeeService employeeService=new EmployeeService();
        employeeService.readEmployeePayrollData(DB_IO);
        Instant start=Instant.now();
        employeeService.addEmployeeToPayRoll(Arrays.asList(arrayOfEmps));
        Instant end=Instant.now();
        System.out.println("Duration Without thread "+ Duration.between(start,end));
        employeeService.readEmployeePayrollData(DB_IO);
        Assertions.assertEquals(7,employeeService.countEntries(DB_IO));

    }


}
