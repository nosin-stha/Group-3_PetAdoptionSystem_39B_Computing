create database PetAdoptionSystem;
use PetAdoptionSystem;

drop database PetAdoptionSystem;
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

create table admin(adminUsername varchar(50), adminPassword varchar(255));

create table otp (
    email varchar(100) primary key,
    otp_code varchar(10) not null,
    created_time timestamp default current_timestamp
);

create table Pets(
petID int auto_increment primary key,
providerID int,
petName varchar(100),
petType enum('Dog','Cat','Bird','Marine','Others'),
petGender enum('Male','Female'),
petAge enum('New Born (<1 Year)','Young (1-3 Years)','Adult (3-8 Years)','Senior (>8 Year)'),
houseTrained enum('Yes','No'),
spayed enum('Yes', 'No'),
vaccinated enum('Yes','No'),
specialNeeds enum('Yes', 'No'),
petAdoptionStatus enum('Adopted','Available') default 'Available', 
imagePath varchar(255),
foreign key (providerID) references Providers(providerID) on delete cascade
);

insert into pets(providerID, petName, petType, petGender, petAge, imagePath) 
values(1, 'Jonny', 'Dog', 'Male', 'Young (1-3 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet1.jpg'),
(1, 'Peepi', 'Bird', 'Male', 'Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet3.jpg'),
(1, 'Hams', 'Others', 'Male', 'New Born (<1 Year)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet4.jpg');
delete from pets where petID=1;
select * from pets;

insert into pets(providerID, petName, petType, petGender, petAge, imagePath) 
values(1, 'yoo', 'Dog','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\logo2.jpg');

delete from pets where petID = 11;
delete from providers where providerID = 6;
select * from Pets;
select * from adopters;
select * from providers;

select * from otp;


create table AdoptionRequests(
adoptionID int auto_increment primary key,
adopterID int,
petID int,
reqFullName varchar(150) not null,
reqEmail varchar(150) not null,
reqPhoneNo varchar(10) not null,
reqAddress varchar(150) not null,
reqReason text,
adoptionStatus enum('Pending','Accepted','Declined') default 'Pending',
foreign key(adopterID) references Adopters(adopterID) on delete cascade,
foreign key(petID) references Pets(petID) on delete cascade);


drop table AdoptionRequests;
insert into AdoptionRequests(adopterID, petID, reqFullName, reqEmail, reqPhoneNo, reqAddress, adoptionStatus) 
values(1,15,'Nosin Shrestha','nosinstha365@gmail.com','9701361444','Kathmandu','Accepted'),
(1,16,'Nosin Shrestha','nosinstha365@gmail.com','9701361444','Kathmandu','Declined'),
(1,17,'Nosin Shrestha','nosinstha365@gmail.com','9701361444','Kathmandu','Pending');




create table AccountReport(
reportID int auto_increment primary key,
adopterID int,
providerID int,
reportReason varchar(255) not null,
reportStatus enum('Pending','Resolved'),
foreign key(adopterID) references Adopters(adopterID) on delete cascade,
foreign key(providerID) references Providers(providerID) on delete cascade
);


create table RecoverRequest(
recoverID int auto_increment primary key,
providerID int,
recoverReqReason varchar(255) not null,
recoverReqStatus enum('Pending','Resolved'),
foreign key(providerID) references Providers(providerID) on delete cascade
);