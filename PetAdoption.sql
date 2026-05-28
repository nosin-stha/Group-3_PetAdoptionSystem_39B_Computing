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
petBreed varchar(150),
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

insert into pets(providerID, petName, petBreed, petGender, petAge, imagePath) 
values(2, 'Jonny', 'Golden Retriever','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet.jpeg'),
(2, 'Jonny', 'Golden Retriever','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet.jpeg'),
(2, 'Jonny', 'Golden Retriever','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet.jpeg'),
(2, 'Jonny', 'Golden Retriever','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet.jpeg'),
(2, 'Jonny', 'Golden Retriever','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\pet.jpeg');
delete from pets where petID=1;
select * from pets;

insert into pets(providerID, petName, petBreed, petGender, petAge, imagePath) 
values(3, 'yoo', 'Golden Retriever','Male','Adult (3-8 Years)', 'C:\\Users\\Dell\\OneDrive\\Documents\\PetAdoptionSystem\\logo2.jpg');

delete from adopters where adopterID = 5;
delete from providers where providerID = 6;
select * from adopters;
select * from providers;

select * from otp;