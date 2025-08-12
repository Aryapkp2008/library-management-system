import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;

public class Library implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Book> books;
    private List<Member> members;
    private List<Transaction> transactions;
    private Map<String, Integer> bookStats; // For tracking popular books

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.bookStats = new HashMap<>();
    }
    
    // Save library data to file
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Library data saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving library data: " + e.getMessage());
        }
    }
    
    // Load library data from file
    public static Library loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No saved library data found. Creating new library.");
            return new Library();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Library library = (Library) ois.readObject();
            System.out.println("Library data loaded successfully from " + filename);
            return library;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading library data: " + e.getMessage());
            return new Library();
        }
    }

    // Book Management Methods
    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    // Member Management Methods
    public void addMember(Member member) {
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    public Member findMemberById(String id) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }

    // Transaction Methods
    public void checkoutBook(String memberId, String bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);
        
        if (member != null && book != null && book.isAvailable()) {
            member.borrowBook(book);
            transactions.add(new Transaction(book, member, Transaction.Type.CHECKOUT));
            
            // Update book stats
            bookStats.put(bookId, bookStats.getOrDefault(bookId, 0) + 1);
            
            System.out.println("Book checked out successfully!");
        } else {
            System.out.println("Unable to checkout the book!");
        }
    }

    public void returnBook(String memberId, String bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);
        
        if (member != null && book != null) {
            if (member.returnBook(book)) {
                transactions.add(new Transaction(book, member, Transaction.Type.RETURN));
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("This member has not borrowed this book!");
            }
        } else {
            System.out.println("Unable to return the book!");
        }
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    // Getters
    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}