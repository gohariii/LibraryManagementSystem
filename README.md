# 📚 Library Management System (Advanced Core)

A professional Java application built to digitize library workflows. This project focuses on high-level management of books and members, with a specialized logic for Loans and Reservations.

---

## 🛠️ System Modules (Management Types)

### 1. Member Management
* **Profile Handling:** Managing member records, contact information, and membership validity.
* **Activity Log:** Viewing the history of books borrowed or reserved by each member.

### 2. Book Management
* **Inventory Control:** Full CRUD operations (Create, Read, Update, Delete) for the book catalog.
* **Status Tracking:** Real-time tracking of book availability (Available, Borrowed, or Reserved).

---

## 🔄 Loan & Reservation Logic (The Workflow)

The system handles two main types of book transactions, ensuring a smooth flow between them:

### 🟢 1. Loan Management (The Primary Action)
* **Direct Borrowing:** If a book is "Available," a loan record is created immediately.
* **Loan Details:** Each loan tracks the issue date, due date, and the specific member involved.
* **Automatic Status Update:** Once a book is loaned, its status automatically changes to "Borrowed," preventing others from taking it.

### 🟡 2. Reservation Management (The Queue System)
* **Booking in Advance:** If a book is currently borrowed, a member can place a "Reservation."
* **Reservation Queue:** The system holds the book for the reserving member as soon as it is returned.

### 🔗 3. The Connection (Reservation to Loan)
* **Seamless Transition:** When a reserved book is returned, the system prompts the librarian to convert that **Reservation** into an active **Loan**.
* **Priority Handling:** The system ensures that a reserved book cannot be loaned to anyone else except the person who reserved it, linking the Reservation ID directly to the new Loan record.

---

## 💻 Technical Stack
* **Language:** Java
* **Database:** SQL (Handles the relational mapping between Members, Books, Loans, and Reservations).
* **Architecture:** MVC-inspired design for clean code separation.
