CREATE DATABASE IF NOT EXISTS customized_chinook;
USE customized_chinook;

CREATE TABLE Employees (
    EmployeeId INT NOT NULL AUTO_INCREMENT,
    LastName VARCHAR(20) NOT NULL,
    FirstName VARCHAR(20) NOT NULL,
    Title VARCHAR(30),
    BirthDate DATE,
    HireDate DATE,
    Email VARCHAR(60),
    Phone VARCHAR(24),
    CONSTRAINT PK_Employees PRIMARY KEY (EmployeeId)
);

CREATE TABLE Customers (
    CustomerId INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(40) NOT NULL,
    LastName VARCHAR(20) NOT NULL,
    Company VARCHAR(80),
    Address VARCHAR(70),
    City VARCHAR(40),
    State VARCHAR(40),
    Country VARCHAR(40),
    Email VARCHAR(60) NOT NULL,
    SupportRepId INT,
    CONSTRAINT PK_Customers PRIMARY KEY (CustomerId),
    CONSTRAINT FK_Customers_SupportRepId FOREIGN KEY (SupportRepId) 
        REFERENCES Employees (EmployeeId) ON DELETE SET NULL
);

CREATE TABLE Location (
    LocationId INT NOT NULL AUTO_INCREMENT,
    City VARCHAR(40) NOT NULL,
    Country VARCHAR(40) NOT NULL,
    CONSTRAINT PK_Location PRIMARY KEY (LocationId)
);

CREATE TABLE Artists (
    ArtistId INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(120),
    LocationId INT,
    CONSTRAINT PK_Artists PRIMARY KEY (ArtistId),
    CONSTRAINT FK_Artists_LocationId FOREIGN KEY (LocationId) 
        REFERENCES Location (LocationId) ON DELETE SET NULL
);

CREATE TABLE Albums (
    AlbumId INT NOT NULL AUTO_INCREMENT,
    Title VARCHAR(160) NOT NULL,
    ArtistId INT NOT NULL,
    CONSTRAINT PK_Albums PRIMARY KEY (AlbumId),
    CONSTRAINT FK_Albums_ArtistId FOREIGN KEY (ArtistId) 
        REFERENCES Artists (ArtistId) ON DELETE CASCADE
);

CREATE TABLE Tracks (
    TrackId INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(200) NOT NULL,
    Composer VARCHAR(220),
    UnitPrice DECIMAL(10, 2) NOT NULL,
    AlbumId INT NOT NULL,
    CONSTRAINT PK_Tracks PRIMARY KEY (TrackId),
    CONSTRAINT FK_Tracks_AlbumId FOREIGN KEY (AlbumId) 
        REFERENCES Albums (AlbumId) ON DELETE CASCADE
);

CREATE TABLE Invoices (
    InvoiceId INT NOT NULL AUTO_INCREMENT,
    InvoiceDate DATE NOT NULL,
    Total DECIMAL(10, 2) NOT NULL,
    CustomerId INT NOT NULL,
    TrackId INT NOT NULL,
    CONSTRAINT PK_Invoices PRIMARY KEY (InvoiceId),
    CONSTRAINT FK_Invoices_CustomerId FOREIGN KEY (CustomerId) 
        REFERENCES Customers (CustomerId) ON DELETE CASCADE,
    CONSTRAINT FK_Invoices_TrackId FOREIGN KEY (TrackId) 
        REFERENCES Tracks (TrackId) ON DELETE CASCADE
);

SHOW TABLES;

DESCRIBE Artists;
DESCRIBE Invoices;