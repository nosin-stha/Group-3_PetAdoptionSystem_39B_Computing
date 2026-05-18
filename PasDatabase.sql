create database PetAdoptionSystem;
use PetAdoptionSystem;
drop database PetAdoptionSystem;

create table Adopters(
adopterID int auto_increment primary key,
adpUsername varchar(20) not null,
adpPassword varchar(50) not null,
adpEmail varchar(100) not null,
adpStatus enum('Active', 'Disabled') default 'Active');

create table Providers(
providerID int auto_increment primary key,
shelterName varchar(255) not null,
proUsername varchar(20) not null,
proPassword varchar(50) not null,
proEmail varchar(100) not null,
proPhoneNo varchar(10) not null,
proStreetAddress varchar(150) not null,
proCity varchar(100),
proDistrict varchar(100) not null,
proStatus enum('Active', 'Disabled', 'Reported') default 'Active');
