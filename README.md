Library Management System (Advanced Core)

This project is a professional Java application built to digitize library workflows. It focuses on high-level management of books and members, with a specialized logic for Loans and Reservations.

System Modules

Member Management: Managing member records and viewing the history of books borrowed or reserved by each member.

Book Management: Full control for the book catalog and real-time tracking of book availability (Available, Borrowed, or Reserved).

Loan and Reservation Logic (The Workflow)

The system handles two main types of book transactions, ensuring a smooth flow between them:

Loan Management (The Primary Action): If a book is Available, a loan record is created immediately. The book status automatically changes to Borrowed, preventing others from taking it.

Reservation Management (The Queue System): If a book is currently borrowed, a member can place a Reservation. The system holds the book in a queue for the reserving member.

The Connection (Reservation to Loan): When a reserved book is returned, the system allows converting that Reservation into an active Loan. Priority Handling: The system ensures that a reserved book cannot be loaned to anyone else except the person who reserved it.

Database Setup

Database Engine: Microsoft SQL Server.

Setup File: You will find the SQL script included in the main directory named LibraryDB_Setup.sql.

Instructions: Simply run this script on your SQL Server to create the database schema and all necessary tables.

Technical Stack

Language: Java Database: SQL Server IDE: NetBeans
