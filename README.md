# 🏙️ SC2002 Build-To-Order (BTO) Management System

A command-line Java application for managing HDB's Build-To-Order flat application process. This project was developed as part of SC2002 - Object-Oriented Design & Programming, demonstrating clean architecture, OOP principles, and real-world modeling of government housing workflows.

---

## 🚀 Features

### 👤 Role-Based Access:
- **Applicant**  
  - View and filter eligible BTO projects
  - Apply for flats based on eligibility
  - Submit and view enquiries
  - Change password

- **HDB Officer**  
  - Register to manage BTO projects (with slot constraints)
  - View and respond to applicant enquiries
  - Assist successful applicants with flat booking
  - Generate booking receipts

- **HDB Manager**  
  - Create, edit, delete, and toggle visibility of BTO projects
  - Approve/reject officer registrations and applicant applications
  - View all enquiries
  - Generate application and booking reports
  - Change password

---

## 🧱 Project Structure

```bash
src/
├── main/Main.java                     # Entry point
├── ui/                               # CLI Menus and Views
├── controller/                       # Handles business logic
├── service/                          # Domain-specific logic
├── data/                             # CSV-based data loading/saving
├── model/
│   ├── user/                         # User classes: Applicant, Officer, Manager
│   ├── project/                      # BTO project and flat types
│   └── transaction/                 # Applications, Enquiries, Bookings
```

## 📁 Data Files

CSV files are used to simulate persistent storage:

- ApplicantList.csv
- ProjectList.csv
- ApplicationList.csv
- EnquiryList.csv
- BookingList.csv

These are located under the /src/data/ directory.

## 💡 Design Principles

- Object-Oriented Design: Strong encapsulation, inheritance, and polymorphism
- Separation of Concerns: Distinct layers for view, controller, model, and data
- Clean Architecture: Controllers handle logic, services support domain rules, UI handles I/O

## 📦 Dependencies

Java 17+
No external libraries used — everything is built with core Java

## 🛠️ How to Run

Clone the repository:
```bash
git clone https://github.com/your-username/sc2002-proj.git
cd sc2002-proj
```

Compile and run:
```bash
javac -d out src/**/*.java
java -cp out main.Main
```

## 📜 License

This project is for educational purposes and is not intended for commercial use.
