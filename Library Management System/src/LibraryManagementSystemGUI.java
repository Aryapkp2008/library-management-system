import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SuppressWarnings("unused")
public class LibraryManagementSystemGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private transient Library library;
    private JTabbedPane tabbedPane;
    private JPanel bookPanel, memberPanel, transactionPanel;
    
    // Book Panel Components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField bookIdField, bookTitleField, bookAuthorField, bookGenreField;
    private JTextField bookSearchField;
    
    // Member Panel Components
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private JTextField memberIdField, memberNameField, memberEmailField, memberPhoneField;
    
    // Transaction Panel Components
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private JComboBox<String> memberComboBox, bookComboBox;
    
    public LibraryManagementSystemGUI() {
        library = new Library();
        initializeLibrary(); // Add sample data
        
        // Setup the GUI after constructor completes
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initializeGUI();
            }
        });
    }
    
    private void initializeGUI() {
        // Setup JFrame
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create tabs
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        createBookPanel();
        createMemberPanel();
        createTransactionPanel();
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Books", bookPanel);
        tabbedPane.addTab("Members", memberPanel);
        tabbedPane.addTab("Transactions", transactionPanel);
        
        // Add to frame
        add(tabbedPane);
        
        // Make visible
        setVisible(true);
    }
    
    private void initializeLibrary() {
        // Add sample books
        library.addBook(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "Classic"));
        library.addBook(new Book("B002", "To Kill a Mockingbird", "Harper Lee", "Fiction"));
        library.addBook(new Book("B003", "1984", "George Orwell", "Dystopian"));
        library.addBook(new Book("B004", "The Hobbit", "J.R.R. Tolkien", "Fantasy"));
        
        // Add sample members
        library.addMember(new Member("M001", "John Doe", "john@example.com", "555-1234"));
        library.addMember(new Member("M002", "Jane Smith", "jane@example.com", "555-5678"));
    }
    
    private void createBookPanel() {
        bookPanel = new JPanel(new BorderLayout());
        
        // Create book table
        String[] bookColumns = {"ID", "Title", "Author", "Genre", "Available"};
        bookTableModel = new DefaultTableModel(bookColumns, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        
        // Create form panel
        JPanel bookFormPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        bookFormPanel.setBorder(BorderFactory.createTitledBorder("Add Book"));
        
        bookIdField = new JTextField(10);
        bookTitleField = new JTextField(20);
        bookAuthorField = new JTextField(20);
        bookGenreField = new JTextField(10);
        
        bookFormPanel.add(new JLabel("Book ID:"));
        bookFormPanel.add(bookIdField);
        bookFormPanel.add(new JLabel("Title:"));
        bookFormPanel.add(bookTitleField);
        bookFormPanel.add(new JLabel("Author:"));
        bookFormPanel.add(bookAuthorField);
        bookFormPanel.add(new JLabel("Genre:"));
        bookFormPanel.add(bookGenreField);
        
        JButton addBookButton = new JButton("Add Book");
        JButton removeBookButton = new JButton("Remove Selected Book");
        
        JPanel bookButtonPanel = new JPanel();
        bookButtonPanel.add(addBookButton);
        bookButtonPanel.add(removeBookButton);
        
        bookFormPanel.add(new JLabel(""));
        bookFormPanel.add(bookButtonPanel);
        
        // Search panel
        JPanel bookSearchPanel = new JPanel();
        bookSearchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));
        
        bookSearchField = new JTextField(20);
        JButton searchTitleButton = new JButton("Search by Title");
        JButton searchAuthorButton = new JButton("Search by Author");
        JButton showAllButton = new JButton("Show All Books");
        
        bookSearchPanel.add(new JLabel("Search:"));
        bookSearchPanel.add(bookSearchField);
        bookSearchPanel.add(searchTitleButton);
        bookSearchPanel.add(searchAuthorButton);
        bookSearchPanel.add(showAllButton);
        
        // Add components to the book panel
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(bookFormPanel, BorderLayout.CENTER);
        southPanel.add(bookSearchPanel, BorderLayout.SOUTH);
        
        bookPanel.add(bookScrollPane, BorderLayout.CENTER);
        bookPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Add event listeners
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });
        
        searchTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooksByTitle();
            }
        });
        
        searchAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooksByAuthor();
            }
        });
        
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBookTable();
            }
        });
        
        // Initialize book table
        refreshBookTable();
    }
    
    private void createMemberPanel() {
        memberPanel = new JPanel(new BorderLayout());
        
        // Create member table
        String[] memberColumns = {"ID", "Name", "Email", "Phone", "Books Borrowed"};
        memberTableModel = new DefaultTableModel(memberColumns, 0);
        memberTable = new JTable(memberTableModel);
        JScrollPane memberScrollPane = new JScrollPane(memberTable);
        
        // Create form panel
        JPanel memberFormPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        memberFormPanel.setBorder(BorderFactory.createTitledBorder("Add Member"));
        
        memberIdField = new JTextField(10);
        memberNameField = new JTextField(20);
        memberEmailField = new JTextField(20);
        memberPhoneField = new JTextField(10);
        
        memberFormPanel.add(new JLabel("Member ID:"));
        memberFormPanel.add(memberIdField);
        memberFormPanel.add(new JLabel("Name:"));
        memberFormPanel.add(memberNameField);
        memberFormPanel.add(new JLabel("Email:"));
        memberFormPanel.add(memberEmailField);
        memberFormPanel.add(new JLabel("Phone:"));
        memberFormPanel.add(memberPhoneField);
        
        JButton addMemberButton = new JButton("Add Member");
        JButton removeMemberButton = new JButton("Remove Selected Member");
        
        JPanel memberButtonPanel = new JPanel();
        memberButtonPanel.add(addMemberButton);
        memberButtonPanel.add(removeMemberButton);
        
        memberFormPanel.add(new JLabel(""));
        memberFormPanel.add(memberButtonPanel);
        
        // Add components to the member panel
        memberPanel.add(memberScrollPane, BorderLayout.CENTER);
        memberPanel.add(memberFormPanel, BorderLayout.SOUTH);
        
        // Add event listeners
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });
        
        removeMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeMember();
            }
        });
        
        // Initialize member table
        refreshMemberTable();
    }
    
    private void createTransactionPanel() {
        transactionPanel = new JPanel(new BorderLayout());
        
        // Create transaction table
        String[] transactionColumns = {"Book", "Member", "Type", "Timestamp"};
        transactionTableModel = new DefaultTableModel(transactionColumns, 0);
        transactionTable = new JTable(transactionTableModel);
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        
        // Create form panel
        JPanel transactionFormPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        transactionFormPanel.setBorder(BorderFactory.createTitledBorder("Book Transactions"));
        
        memberComboBox = new JComboBox<>();
        bookComboBox = new JComboBox<>();
        
        updateComboBoxes();
        
        transactionFormPanel.add(new JLabel("Member:"));
        transactionFormPanel.add(memberComboBox);
        transactionFormPanel.add(new JLabel("Book:"));
        transactionFormPanel.add(bookComboBox);
        
        JButton checkoutButton = new JButton("Checkout Book");
        JButton returnButton = new JButton("Return Book");
        
        JPanel transactionButtonPanel = new JPanel();
        transactionButtonPanel.add(checkoutButton);
        transactionButtonPanel.add(returnButton);
        
        transactionFormPanel.add(new JLabel(""));
        transactionFormPanel.add(transactionButtonPanel);
        
        // Add components to the transaction panel
        transactionPanel.add(transactionScrollPane, BorderLayout.CENTER);
        transactionPanel.add(transactionFormPanel, BorderLayout.SOUTH);
        
        // Add event listeners
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutBook();
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });
        
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() == transactionPanel) {
                    updateComboBoxes();
                    refreshTransactionTable();
                }
            }
        });
        
        // Initialize transaction table
        refreshTransactionTable();
    }
    
    private void updateComboBoxes() {
        memberComboBox.removeAllItems();
        bookComboBox.removeAllItems();
        
        for (Member member : library.getMembers()) {
            memberComboBox.addItem(member.getId() + " - " + member.getName());
        }
        
        for (Book book : library.getBooks()) {
            if (book.isAvailable()) {
                bookComboBox.addItem(book.getId() + " - " + book.getTitle());
            }
        }
    }
    
    private void refreshBookTable() {
        bookTableModel.setRowCount(0);
        
        for (Book book : library.getBooks()) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.isAvailable() ? "Yes" : "No"
            };
            bookTableModel.addRow(row);
        }
    }
    
    private void refreshMemberTable() {
        memberTableModel.setRowCount(0);
        
        for (Member member : library.getMembers()) {
            Object[] row = {
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getBorrowedBooks().size()
            };
            memberTableModel.addRow(row);
        }
    }
    
    private void refreshTransactionTable() {
        transactionTableModel.setRowCount(0);
        
        for (Transaction transaction : library.getTransactions()) {
            Object[] row = {
                transaction.getBook().getTitle(),
                transaction.getMember().getName(),
                transaction.getType().toString(),
                transaction.getTimestamp().toString()
            };
            transactionTableModel.addRow(row);
        }
    }
    
    private void addBook() {
        String id = bookIdField.getText().trim();
        String title = bookTitleField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String genre = bookGenreField.getText().trim();
        
        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = new Book(id, title, author, genre);
        library.addBook(book);
        
        refreshBookTable();
        clearBookFields();
        JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void removeBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to remove", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String bookId = (String) bookTable.getValueAt(selectedRow, 0);
        Book book = library.findBookById(bookId);
        
        if (book != null) {
            library.removeBook(book);
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Book removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchBooksByTitle() {
        String searchTerm = bookSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Book> books = library.findBooksByTitle(searchTerm);
        bookTableModel.setRowCount(0);
        
        for (Book book : books) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.isAvailable() ? "Yes" : "No"
            };
            bookTableModel.addRow(row);
        }
    }
    
    private void searchBooksByAuthor() {
        String searchTerm = bookSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Book> books = library.findBooksByAuthor(searchTerm);
        bookTableModel.setRowCount(0);
        
        for (Book book : books) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.isAvailable() ? "Yes" : "No"
            };
            bookTableModel.addRow(row);
        }
    }
    
    private void clearBookFields() {
        bookIdField.setText("");
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookGenreField.setText("");
    }
    
    private void addMember() {
        String id = memberIdField.getText().trim();
        String name = memberNameField.getText().trim();
        String email = memberEmailField.getText().trim();
        String phone = memberPhoneField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Member member = new Member(id, name, email, phone);
        library.addMember(member);
        
        refreshMemberTable();
        clearMemberFields();
        JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void removeMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to remove", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String memberId = (String) memberTable.getValueAt(selectedRow, 0);
        Member member = library.findMemberById(memberId);
        
        if (member != null) {
            library.removeMember(member);
            refreshMemberTable();
            JOptionPane.showMessageDialog(this, "Member removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearMemberFields() {
        memberIdField.setText("");
        memberNameField.setText("");
        memberEmailField.setText("");
        memberPhoneField.setText("");
    }
    
    private void checkoutBook() {
        if (memberComboBox.getSelectedIndex() == -1 || bookComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member and a book", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String memberIdWithName = (String) memberComboBox.getSelectedItem();
        String bookIdWithTitle = (String) bookComboBox.getSelectedItem();
        
        String memberId = memberIdWithName.split(" - ")[0];
        String bookId = bookIdWithTitle.split(" - ")[0];
        
        library.checkoutBook(memberId, bookId);
        
        refreshBookTable();
        refreshMemberTable();
        refreshTransactionTable();
        updateComboBoxes();
        
        JOptionPane.showMessageDialog(this, "Book checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void returnBook() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to return the book", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String bookTitle = (String) transactionTable.getValueAt(selectedRow, 0);
        String memberName = (String) transactionTable.getValueAt(selectedRow, 1);
        String type = (String) transactionTable.getValueAt(selectedRow, 2);
        
        if (!type.equals("CHECKOUT")) {
            JOptionPane.showMessageDialog(this, "Selected transaction is not a checkout", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = null;
        Member member = null;
        
        for (Book b : library.getBooks()) {
            if (b.getTitle().equals(bookTitle)) {
                book = b;
                break;
            }
        }
        
        for (Member m : library.getMembers()) {
            if (m.getName().equals(memberName)) {
                member = m;
                break;
            }
        }
        
        if (book != null && member != null) {
            library.returnBook(member.getId(), book.getId());
            
            refreshBookTable();
            refreshMemberTable();
            refreshTransactionTable();
            updateComboBoxes();
            
            JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibraryManagementSystemGUI();
            }
        });
    }
} 