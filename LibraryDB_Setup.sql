CREATE DATABASE [LibrarySystemDB];
USE [LibrarySystemDB];

-- 1. Categories Table
CREATE TABLE [dbo].[Categories](
	[CategoryID] [int] IDENTITY(1,1) PRIMARY KEY,
	[Name] [varchar](100) UNIQUE NOT NULL,
	[Description] [varchar](255) NULL
);

-- 2. Books Table
CREATE TABLE [dbo].[Books](
	[BookID] [int] IDENTITY(1,1) PRIMARY KEY,
	[Title] [varchar](255) NOT NULL,
	[Author] [varchar](100) NOT NULL,
	[ISBN] [varchar](20) UNIQUE NOT NULL,
	[PublishedYear] [int] NULL,
	[TotalCopies] [int] NOT NULL,
	[CopiesAvailable] [int] NOT NULL,
	[CategoryID] [int] FOREIGN KEY REFERENCES [dbo].[Categories]([CategoryID])
);

-- 3. Admins Table
CREATE TABLE [dbo].[Admins](
	[AdminID] [int] IDENTITY(1,1) PRIMARY KEY,
	[Name] [varchar](100) NOT NULL,
	[Username] [varchar](50) UNIQUE NOT NULL,
	[PasswordHash] [varchar](255) NOT NULL,
	[Email] [varchar](100) UNIQUE NULL,
	[Role] [varchar](50) NOT NULL
);

-- 4. Members Table
CREATE TABLE [dbo].[Members](
	[MemberID] [int] IDENTITY(1,1) PRIMARY KEY,
	[Name] [varchar](100) NULL,
	[Username] [varchar](50) UNIQUE NULL,
	[Email] [varchar](100) UNIQUE NULL,
	[JoinDate] [datetime] DEFAULT GETDATE()
);

-- 5. Loans Table
CREATE TABLE [dbo].[Loans](
	[LoanID] [int] IDENTITY(1,1) PRIMARY KEY,
	[BookID] [int] FOREIGN KEY REFERENCES [dbo].[Books]([BookID]),
	[MemberID] [int] FOREIGN KEY REFERENCES [dbo].[Members]([MemberID]),
	[LoanDate] [datetime] DEFAULT GETDATE(),
	[DueDate] [datetime] NOT NULL,
	[ActualReturnDate] [datetime] NULL,
	[FineAmount] [decimal](10, 2) DEFAULT 0.00
);

-- 6. Reservations Table
CREATE TABLE [dbo].[Reservations](
	[ReservationID] [int] IDENTITY(1,1) PRIMARY KEY,
	[BookID] [int] FOREIGN KEY REFERENCES [dbo].[Books]([BookID]),
	[MemberID] [int] FOREIGN KEY REFERENCES [dbo].[Members]([MemberID]),
	[ReservationDate] [datetime] DEFAULT GETDATE(),
	[Status] [varchar](20) DEFAULT 'PENDING'
);