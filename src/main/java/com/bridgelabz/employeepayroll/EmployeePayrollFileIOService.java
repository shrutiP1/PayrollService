package com.bridgelabz.employeepayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService
{
    public static String PAYROLL_FILE_NAME="payroll-file.txt";
    public static void writeData(List<EmployeePayRollData> employeePayRollList)
    {
        StringBuffer empBuffer=new StringBuffer();
        employeePayRollList.forEach(employee ->{
            String employeeDataString=employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });
        try
        {
            Files.write(Paths.get(PAYROLL_FILE_NAME),empBuffer.toString().getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void printData()
    {
        try
        {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static long countEntries()
    {
        long entries=0;
        try
        {
            entries=Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return entries;
    }
    public static List<EmployeePayRollData> readData()
    {
        List<EmployeePayRollData> employeePayrollList=new ArrayList<>();
        try
        {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath())
                    .map(line->line.trim())
                    .forEach(line->System.out.println(line));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}
