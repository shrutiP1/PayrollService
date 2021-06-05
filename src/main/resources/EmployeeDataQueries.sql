#UC1
create database payroll_service;
USE payroll_service;
SHOW databases;

#UC2
create table employee_payroll
(
   id int unsigned not null auto_increment,
   name varchar(25) not null,
   salary double not null,
   start date not null,
   primary key(id)
);

#UC3
DROP TABLE employee_payroll;
DESCRIBE employee_payroll;
INSERT into employee_payroll(name,salary,start)values
                                 ('Barlie',1000000,'2018-01-03'),
                                 ('Terisa',2000000,'2019-02-03'),
                                 ('Charlie',3000000,'2020-03-09');
#UC4
SELECT*FROM employee_payroll;

#UC5
SELECT*FROM employee_payroll WHERE name='Barlie';
DELETE FROM employee_payroll WHERE name='Barlie';
INSERT INTO employee_payroll(name,salary,start)VALUES('Barlie',1000000,'2018-01-03');
SELECT*FROM employee_payroll WHERE start BETWEEN cast('2015-02-03'AS DATE) AND DATE(NOW());

#UC6
ALTER TABLE employee_payroll
ADD gender char(1) AFTER name;
SELECT*FROM employee_payroll;
UPDATE employee_payroll set gender='f' WHERE name='Barlie' OR name='Charlie';

#UC7
SELECT SUM(salary) from employee_payroll WHERE gender='f' GROUP BY gender;
SELECT gender,AVG(salary) from employee_payroll GROUP BY gender;
SELECT gender,MIN(salary) from employee_payroll GROUP BY gender;
SELECT gender,MAX(salary) from employee_payroll GROUP BY gender;
SELECT gender,COUNT(salary) from employee_payroll GROUP BY gender;

#UC8
desc employee_payroll;
alter table employee_payroll1
add phone_number varchar(256) after start,
add address varchar(256) default 'pune' after phone_number,
add department varchar(35) not null after address ;

 #UC9
 alter table employee_payroll
 rename column salary to basic_pay;
 alter table employee_payroll
 add deductions double after basic_pay,
 add teaxable_pay double after deductions,
 add tax double after teaxable_pay,
 add net_pay double after tax;

#UC10
desc employee_payroll;
select*from employee_payroll;
delete from employee_payroll where id=4;
INSERT INTO employee_payroll
(name, department, gender, basic_pay, deductions, taxable_pay, tax, net_pay, start) VALUES('Terisa','Markeing','f',200000,100000,500000,150000,200000,'2010-01-03');

#UC11
create table company(
company_id int primary key,
company_name varchar(60) unique
);
describe company;
insert into company values(1,'Tcs') ,(2,'Wipro');

create table employee1(
 emp_id int auto_increment  primary key ,
 name varchar(20) not null,
 phone_number int,
 address varchar(30),
 gender char(1),
 start Date,
 company_id int not null,
 foreign key(company_id) references company(company_id)
 ON DELETE CASCADE
ON UPDATE CASCADE
 );
insert into employee1(name,phone_number,address,gender,start,company_id) values('Berlin',91304266,'pune','f','2020-09-03',1);
insert into employee1(name,phone_number,address,gender,start,company_id) values('Terisa',9230303,'Mumbai','m','2020-09-03',2);
insert into employee1(name,phone_number,address,gender,start,company_id) values('charlie',0002020,'pune','f','2020-09-03',1);

create table payroll_db
(
  pay_id int auto_increment primary key,
  basic_pay float not null,
  deductions float ,
  texable_pay float,
  tax float ,
  net_pay float,
  emp int not null,
  foreign key (emp) references employee1(emp_id) ON DELETE CASCADE
ON UPDATE CASCADE
);
insert into payroll_db (basic_pay,emp) values(100000,1);
insert into payroll_db(basic_pay,emp) values(2000000,3);
insert into payroll_db(basic_pay,emp) values(3000000,4);

CREATE TABLE department_data
(
    department_id int auto_increment primary key,
    department_name varchar(50) not null,
    employee_id int not null,
     foreign key (employee_id) references employee1(emp_id)
    ON DELETE CASCADE
	ON UPDATE CASCADE
);
insert into department_data values(1,'Marketing',3);
insert into department_data values(2,'Sales',3);
insert into department_data values(3,'Hr',1);
insert into department_data values(4,'Admin',4);

create table department_employee_tb(
 department_employee_id int auto_increment primary key,
 depref_id int not null,
 empref_id int not null,
 foreign key(depref_id) references department_data(department_id) ,
 foreign key(empref_id) references employee1(emp_id)
);
insert into department_employee_tb values(1,1,3);
insert into department_employee_tb values(2,2,3);
insert into department_employee_tb values(3,3,3);
insert into department_employee_tb values(4,4,4);

#UC12
select*from company c,employee1 e where c.company_id=e.company_id ;
select company.company_name, employee1.name from company INNER JOIN employee1 ON company.company_id=employee1.company_id;
select employee1.name ,payroll_db.basic_pay from employee1 INNER JOIN payroll_db ON employee1. emp_id=payroll_db.emp where employee1.name='Berlin';
select *from employee1 where start BETWEEN CAST('2019-01-03' as DATE) AND DATE(now());
select employee1.gender ,sum(payroll_db.basic_pay) as sum from employee1 INNER JOIN payroll_db ON  emp_id=payroll_db.emp where employee1.gender='f' group by employee1.gender ;
select employee1.gender ,MAX(payroll_db.basic_pay) as sum from employee1 INNER JOIN payroll_db ON  emp_id=payroll_db.emp  group by employee1.gender ;
select employee1.gender ,MIN(payroll_db.basic_pay) as sum from employee1 INNER JOIN payroll_db ON  emp_id=payroll_db.emp  group by employee1.gender ;
select employee1.gender ,COUNT(payroll_db.basic_pay) as sum from employee1 INNER JOIN payroll_db ON  emp_id=payroll_db.emp  group by employee1.gender ;

#UC1(JDBC)
create table emp_payroll(
id int primary key auto_increment,
name varchar(60),
salary double,
start date);

desc emp_payroll;
select*from emp_payroll;
insert into emp_payroll(name,salary,start)values('Bill',10000000,date('2020-01-03'));
insert into emp_payroll(name,salary,start)values('Terisa',20000000,date('2019-11-13'));
insert into emp_payroll(name,salary,start)values('Charlie',30000000,date('2020-12-13'));
select *from emp_payroll;

