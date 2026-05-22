create database PetAdoptionSystem;
use PetAdoptionSystem;


create table Adopters(
adopterID int auto_increment primary key,
adpUsername varchar(50) not null,
adpPassword varchar(50) not null,
adpEmail varchar(100) not null unique,
adpStatus enum('Active', 'Disabled') default 'Active');
alter table Adopters add column adpPfp varchar(255);

create table Providers(
providerID int auto_increment primary key,
shelterName varchar(255) not null,
licenseID varchar(20) not null,
proUsername varchar(50) not null,
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

alter table Providers add column proPfp varchar(255);

create table otp (
    email varchar(100) primary key,
    otp_code varchar(10) not null,
    created_time timestamp default current_timestamp
);

delete from adopters where adopterID = 2;
delete from providers where providerID = 1;
select * from adopters;
select * from providers;

select * from otp;
