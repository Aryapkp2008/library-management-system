import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Add some sample data
        initializeLibrary();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    bookManagement();
                    break;
                case 2:
                    memberManagement();
                    break;
                case 3:
                    transactionManagement();
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using the Library Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    private static void initializeLibrary() {
        // Add sample books
        library.addBook(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "Classic"));
        library.addBook(new Book("B002", "To Kill a Mockingbird", "Harper Lee", "Fiction"));
        library.addBook(new Book("B003", "1984", "George Orwell", "Dystopian"));
        library.addBook(new Book("B004", "The Hobbit", "J.R.R. Tolkien", "Fantasy"));
        
        // Add sample members
        library.addMember(new Member("M001", "John Doe", "john@example.com", "555-1234"));
        library.addMember(new Member("M002", "Jane Smith", "jane@example.com", "555-5678"));
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Transaction Management");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void bookManagement() {
        while (true) {
            System.out.println("\n===== Book Management =====");
            System.out.println("1. Add a new book");
            System.out.println("2. Remove a book");
            System.out.println("3. Find a book by ID");
            System.out.println("4. Find books by title");
            System.out.println("5. Find books by author");
            System.out.println("6. Display all books");
            System.out.println("7. Return to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    findBookById();
                    break;
                case 4:
                    findBooksByTitle();
                    break;
                case 5:
                    findBooksByAuthor();
                    break;
                case 6:
                    displayAllBooks();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void memberManagement() {
        while (true) {
            System.out.println("\n===== Member Management =====");
            System.out.println("1. Add a new member");
            System.out.println("2. Remove a member");
            System.out.println("3. Find a member by ID");
            System.out.println("4. Display all members");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    removeMember();
                    break;
                case 3:
                    findMemberById();
                    break;
                case 4:
                    displayAllMembers();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void transactionManagement() {
        while (true) {
            System.out.println("\n===== Transaction Management =====");
            System.out.println("1. Check out a book");
            System.out.println("2. Return a book");
            System.out.println("3. View all transactions");
            System.out.println("4. Return to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    checkoutBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewAllTransactions();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // Book management methods
    private static void addBook() {
        System.out.print("Enter book ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        
        Book book = new Book(id, title, author, genre);
        library.addBook(book);
        System.out.println("Book added successfully!");
    }
    
    private static void removeBook() {
        System.out.print("Enter book ID to remove: ");
        String id = scanner.nextLine();
        
        Book book = library.findBookById(id);
        if (book != null) {
            library.removeBook(book);
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }
    
    private static void findBookById() {
        System.out.print("Enter book ID: ");
        String id = scanner.nextLine();
        
        Book book = library.findBookById(id);
        if (book != null) {
            System.out.println("Book found: " + book);
        } else {
            System.out.println("Book not found!");
        }
    }
    
    private static void findBooksByTitle() {
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine();
        
        List<Book> books = library.findBooksByTitle(title);
        if (!books.isEmpty()) {
            System.out.println("Books found:");
            for (Book book : books) {
                System.out.println(book);
            }
        } else {
            System.out.println("No books found with that title!");
        }
    }
    
    private static void findBooksByAuthor() {
        System.out.print("Enter author to search: ");
        String author = scanner.nextLine();
        
        List<Book> books = library.findBooksByAuthor(author);
        if (!books.isEmpty()) {
            System.out.println("Books found:");
            for (Book book : books) {
                System.out.println(book);
            }
        } else {
            System.out.println("No books found by that author!");
        }
    }
    
    private static void displayAllBooks() {
        List<Book> books = library.getBooks();
        if (!books.isEmpty()) {
            System.out.println("All Books:");
            for (Book book : books) {
                System.out.println(book);
            }
        } else {
            System.out.println("No books in the library!");
        }
    }
    
    // Member management methods
    private static void addMember() {
        System.out.print("Enter member ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        
        Member member = new Member(id, name, email, phoneNumber);
        library.addMember(member);
        System.out.println("Member added successfully!");
    }
    
    private static void removeMember() {
        System.out.print("Enter member ID to remove: ");
        String id = scanner.nextLine();
        
        Member member = library.findMemberById(id);
        if (member != null) {
            library.removeMember(member);
            System.out.println("Member removed successfully!");
        } else {
            System.out.println("Member not found!");
        }
    }
    
    private static void findMemberById() {
        System.out.print("Enter member ID: ");
        String id = scanner.nextLine();
        
        Member member = library.findMemberById(id);
        if (member != null) {
            System.out.println("Member found: " + member);
        } else {
            System.out.println("Member not found!");
        }
    }
    
    private static void displayAllMembers() {
        List<Member> members = library.getMembers();
        if (!members.isEmpty()) {
            System.out.println("All Members:");
            for (Member member : members) {
                System.out.println(member);
            }
        } else {
            System.out.println("No members in the library!");
        }
    }
    
    // Transaction management methods
    private static void checkoutBook() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();
        
        library.checkoutBook(memberId, bookId);
    }
    
    private static void returnBook() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();
        
        library.returnBook(memberId, bookId);
    }
    
    private static void viewAllTransactions() {
        List<Transaction> transactions = library.getTransactions();
        if (!transactions.isEmpty()) {
            System.out.println("All Transactions:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        } else {
            System.out.println("No transactions in the library!");
        }
    }
} 