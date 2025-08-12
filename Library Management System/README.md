# Library Management System

A Java application for managing a library's books, members, and transactions. Available in both console and GUI versions. The system now includes data persistence and enhanced book management features.

## Features

- **Book Management**: Add, remove, and search for books by ID, title, or author
- **Enhanced Book Details**: Track ISBN, publication year, publisher, and multiple copies
- **Member Management**: Add, remove, and search for library members
- **Transaction Management**: Check out books, return books, and view transaction history
- **Data Persistence**: Save and load library data between sessions
- **Book Statistics**: Track popular books based on checkout frequency

## Project Structure

```
Library Management System/
├── src/
│   ├── Book.java                   # Book data model
│   ├── Member.java                 # Member data model
│   ├── Transaction.java            # Transaction data model
│   ├── Library.java                # Core library functionality
│   ├── LibraryManagementSystem.java # Console UI application
│   └── LibraryManagementSystemGUI.java # Graphical UI application
└── README.md
```

## How to Compile and Run

1. Navigate to the project directory:
   ```
   cd "Library Management System"
   ```

2. Use the build script to compile:
   ```
   build.bat
   ```

3. Run the console application:
   ```
   run-console.bat
   ```

4. Or run the GUI application:
   ```
   run-gui.bat
   ```

5. Alternatively, you can compile and run manually:
   ```
   javac src/*.java
   java -cp src LibraryManagementSystem
   java -cp src LibraryManagementSystemGUI
   ```

## Usage

1. When you run the application, you'll see a main menu with the following options:
   - Book Management
   - Member Management
   - Transaction Management
   - Exit

2. Select an option by entering the corresponding number.

3. Follow the prompts to perform various operations like adding books, registering members, checking out books, etc.

## Sample Data

The application comes pre-loaded with sample data:

### Books:
- The Great Gatsby by F. Scott Fitzgerald
- To Kill a Mockingbird by Harper Lee
- 1984 by George Orwell
- The Hobbit by J.R.R. Tolkien

### Members:
- John Doe (john@example.com)
- Jane Smith (jane@example.com)

## Extending the Project

You can extend this project by:
1. Adding a database for persistent storage
2. Enhancing the GUI with additional features
3. Adding more features like fines for late returns
4. Adding book categories and advanced search functionality

## GitHub Access

This project is available on GitHub, allowing you to:
1. Access the code from anywhere
2. Track changes and collaborate with others
3. Clone the repository to work on it locally
4. Submit issues and feature requests

To clone this repository:
```
git clone https://github.com/YOUR-USERNAME/library-management-system.git
cd library-management-system
```

Replace `YOUR-USERNAME` with your actual GitHub username after setting up the repository.