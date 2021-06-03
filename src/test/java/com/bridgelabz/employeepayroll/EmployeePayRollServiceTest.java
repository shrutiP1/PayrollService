package com.bridgelabz.employeepayroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.bridgelabz.employeepayroll.EmployeeService.ioService.DB_IO;
import static com.bridgelabz.employeepayroll.EmployeeService.ioService.FILE_IO;

public class EmployeePayRollServiceTest
{
    @Test
    public void given3EmployeeSwhenWrittenToFileShouldMatchEmployeeEntries()
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
    public void givenEmployeePayrollDB_WhenRetrieved_shouldMatchEmployeeCount()
    {
        EmployeeService employeeService=new EmployeeService();
        List<EmployeePayRollData> employeePayRollData=employeeService.readEmployeePayrollData(DB_IO);
        Assertions.assertEquals(3,employeePayRollData.size());
    }
}