create database PetAdoptionSystem;
use PetAdoptionSystem;


create table Adopters(
adopterID int auto_increment primary key,
adpUsername varchar(20) not null,
adpPassword varchar(50) not null,
adpEmail varchar(100) not null unique,
adpStatus enum('Active', 'Disabled') default 'Active');

alter table adopters modify column adpUsername varchar(12) not null unique;

create table Providers(
providerID int auto_increment primary key,
shelterName varchar(255) not null,
licenseID varchar(20) not null,
proUsername varchar(20) not null,
proPassword varchar(50) not null,
proEmail varchar(100) not null unique,
proPhoneNo varchar(10) not null,
proAddress varchar(150) not null,
startWorkHour varchar(20),
endWorkHour varchar(20),
startWorkDay varchar(20),
endWorkDay varchar(20),
proMissionStatement text,
proAdoptionPolicy text,
proStatus enum('Active', 'Disabled', 'Reported') default 'Active');

create table otp (
    email varchar(100) primary key,
    otp_code varchar(10) not null,
    created_time timestamp default current_timestamp
);


select * from adopters;
select * from providers;
select * from otp;
