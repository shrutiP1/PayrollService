package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayRollData
{
     String gender;
    LocalDate startDate;
    int id;
    String name;
    double salary;

    public EmployeePayRollData(Integer id, String name, Double salary)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
    public EmployeePayRollData(Integer id, String name, Double salary,LocalDate startDate)
    {
        this(id, name, salary);
        this.startDate=startDate;
    }

    public EmployeePayRollData(int id, String name, String gender, double salary, LocalDate startDate)
    {
        this(id,name,salary,startDate);
        this.gender=gender;

    }


    @Override
    public String toString() {
        return "EmployeePayRollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayRollData that = (EmployeePayRollData) o;
        return id == that.id && Double.compare(that.salary, salary) == 0 && Objects.equals(startDate, that.startDate) && Objects.equals(name, that.name);
    }

}
