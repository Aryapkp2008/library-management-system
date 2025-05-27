import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Library implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private transient List<Book> books;
    private transient List<Member> members;
    private transient List<Transaction> transactions;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.transactions = new ArrayList<>();
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
            System.out.println("Book checked out successfully!");
        } else {
            System.out.println("Unable to checkout the book!");
        }
    }

    public void returnBook(String memberId, String bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);
        
        if (member != null && book != null && !book.isAvailable()) {
            member.returnBook(book);
            transactions.add(new Transaction(book, member, Transaction.Type.RETURN));
            System.out.println("Book returned successfully!");
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