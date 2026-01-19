# Expense Tracker

A Java-based Expense Tracker application that helps users manage and track their expenses efficiently.

## 📋 About
This project allows users to record and categorize daily expenses, view balances, and monitor spending habits. Built with Java and MySQL, it features a Swing GUI for an intuitive desktop experience.

## 🛠️ Features
- Add new expense entries with details like amount, category, and date
- View and manage all recorded expenses
- MySQL database for persistent data storage
- Desktop GUI built with Java Swing
- Organized codebase for maintainability and future enhancements

## 🚀 Getting Started

### Prerequisites
Make sure you have the following installed:
- **Java JDK 17** or above
- **Maven** (for build and dependency management)
- **MySQL Server** (for database)

### Installation & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/DhaanishNihaal/expense_tracker.git
   ```

2. **Navigate to the project folder**
   ```bash
   cd expense_tracker
   ```

3. **Set up database configuration**
   - Create a `.env` file in the project root (if needed)
   - Configure your MySQL database credentials

4. **Run the application using Maven**
   ```bash
   mvn clean compile exec:java
   ```

   **Or build and run as JAR:**
   ```bash
   mvn clean package
   java -jar target/todo-app-0.0.1-SNAPSHOT.jar
   ```

## 📁 Project Structure
```
expense_tracker/
├── src/
│   └── main/java/com/expense/
├── assets/
├── pom.xml
└── README.md
```

## 💻 Technologies Used
- Java 17
- MySQL Database
- Java Swing (GUI)
- Maven
- dotenv-java (environment configuration)

## 📝 License
This project is open source and available for educational purposes.
