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

create table Pets(
petID int auto_increment primary key,
providerID int,
petName varchar(100),
petBreed varchar(150),
petGender enum('Male','Female'),
petAge enum('Puppy (<1 Year)','Young (1-3 Years)','Adult (3-8 Years)','Senior (>8 Year)'),
houseTrained enum('Yes','No'),
spayed enum('Yes', 'No'),
vaccinated enum('Yes','No'),
specialNeeds enum('Yes', 'No'),
petAdoptionStatus enum('Adopted','Available') default 'Available', 
foreign key (providerID) references Providers(providerID) on delete cascade
);

create table AdoptionRequests(
adoptionID int auto_increment primary key,
adopterID int,
petID int,
reqFullName varchar(150) not null,
reqEmail varchar(150) not null,
reqPhoneNo varchar(10) not null,
reqStreetAddress varchar(150) not null,
reqCity varchar(100),
reqDistrict varchar(100) not null,
adoptionStatus enum('Pending','Accepted','Declined') default 'Pending',
foreign key(adopterID) references Adopters(adopterID) on delete cascade,
foreign key(petID) references Pets(petID) on delete cascade);

create table ReportedAccounts(
reportID int auto_increment primary key,
adopterID int,
providerID int,
reportStatus enum('Pending','Resolved'),
foreign key(adopterID) references Adopters(adopterID) on delete cascade,
foreign key(providerID) references Providers(providerID) on delete cascade
);
alter table ReportedAccounts add column reportReason varchar(255) not null;

create table RecoverAccount(
recoverID int auto_increment primary key,
providerID int,
recoverReqReason varchar(255) not null,
recoverReqStatus enum('Pending','Resolved'),
foreign key(providerID) references Providers(providerID) on delete cascade
);