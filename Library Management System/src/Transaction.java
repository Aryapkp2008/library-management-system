import java.time.LocalDateTime;
import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {
        CHECKOUT, RETURN
    }

    private Book book;
    private Member member;
    private Type type;
    private LocalDateTime timestamp;

    public Transaction(Book book, Member member, Type type) {
        this.book = book;
        this.member = member;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "book=" + book.getTitle() +
                ", member=" + member.getName() +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
} 