create database if not exists employee;
use employee;
create table employee(

    id varchar(10) primary key,
    name varchar(30) not null,
    city varchar(30) not null,
    salary double not null,
    joiningDate date not null,
    position varchar(50)

)