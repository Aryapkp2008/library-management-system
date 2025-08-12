import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings("unused")
public class LibraryManagementSystemGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210); // Material Blue
    private static final Color SECONDARY_COLOR = new Color(66, 165, 245); // Lighter Blue
    private static final Color ACCENT_COLOR = new Color(255, 193, 7); // Amber
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark Gray
    
    private transient Library library;
    private JTabbedPane tabbedPane;
    private JPanel bookPanel, memberPanel, transactionPanel;
    private JLabel statusLabel;
    private JPanel statusBar;
    
    // Book Panel Components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private TableRowSorter<DefaultTableModel> bookTableSorter;
    private JTextField bookIdField, bookTitleField, bookAuthorField, bookGenreField;
    private JTextField bookSearchField;
    private JComboBox<String> bookGenreComboBox;
    
    // Member Panel Components
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private TableRowSorter<DefaultTableModel> memberTableSorter;
    private JTextField memberIdField, memberNameField, memberEmailField, memberPhoneField;
    private JTextField memberSearchField;
    
    // Transaction Panel Components
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private TableRowSorter<DefaultTableModel> transactionTableSorter;
    private JComboBox<String> memberComboBox;
    private JComboBox<String> bookComboBox;
    private JComboBox<String> transactionTypeComboBox;
    private JTextField transactionSearchField;
    
    public LibraryManagementSystemGUI() {
        library = new Library();
        initializeLibrary(); // Add sample data
        
        // Setup the GUI after constructor completes
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set the look and feel to the system look and feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
                    // Customize UI components
                    customizeUIComponents();
                    
                    initializeGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void customizeUIComponents() {
        // Customize button appearance
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        
        // Customize text fields
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", PRIMARY_COLOR);
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        
        // Customize tables
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("Table.selectionBackground", SECONDARY_COLOR);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", new Color(220, 220, 220));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        
        // Customize tabbed pane
        UIManager.put("TabbedPane.selected", SECONDARY_COLOR);
        UIManager.put("TabbedPane.background", BACKGROUND_COLOR);
        UIManager.put("TabbedPane.foreground", TEXT_COLOR);
        UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 14));
    }
    
    private void initializeGUI() {
        // Setup JFrame
        setTitle("Library Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create tabs with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Initialize panels
        createBookPanel();
        createMemberPanel();
        createTransactionPanel();
        
        // Add tabs to tabbed pane with icons
        ImageIcon bookIcon = createIcon("📚", 16);
        ImageIcon memberIcon = createIcon("👤", 16);
        ImageIcon transactionIcon = createIcon("🔄", 16);
        
        tabbedPane.addTab("Books", bookIcon, bookPanel);
        tabbedPane.addTab("Members", memberIcon, memberPanel);
        tabbedPane.addTab("Transactions", transactionIcon, transactionPanel);
        
        // Create status bar
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(PRIMARY_COLOR);
        statusBar.setPreferredSize(new Dimension(getWidth(), 30));
        
        // Add current date/time to status bar
        JLabel dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Update date/time every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                dateTimeLabel.setText(sdf.format(new Date()));
            }
        });
        timer.start();
        
        // Add status message label
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        statusBar.add(dateTimeLabel, BorderLayout.WEST);
        statusBar.add(statusLabel, BorderLayout.EAST);
        
        // Add components to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
        
        // Make visible
        setVisible(true);
        
        // Set initial status message
        updateStatusMessage("Library Management System loaded successfully");
    }
    
    private ImageIcon createIcon(String text, int size) {
        // Create a label with the text
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, size));
        
        // Create an image of the label
        BufferedImage image = new BufferedImage(
            size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        label.paint(g2d);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    private void updateStatusMessage(String message) {
        statusLabel.setText(message);
        
        // Reset status message after 5 seconds
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Ready");
            }
        });
        timer.setRepeats(false);
        timer.start();
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
        bookPanel = new JPanel(new BorderLayout(10, 10));
        bookPanel.setBackground(BACKGROUND_COLOR);
        bookPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create book table with enhanced styling
        String[] bookColumns = {"ID", "Title", "Author", "Genre", "Available"};
        bookTableModel = new DefaultTableModel(bookColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        bookTable = new JTable(bookTableModel);
        bookTable.setRowHeight(30);
        bookTable.setIntercellSpacing(new Dimension(10, 5));
        bookTable.setGridColor(new Color(230, 230, 230));
        bookTable.setSelectionBackground(SECONDARY_COLOR);
        bookTable.setSelectionForeground(Color.WHITE);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookTable.setAutoCreateRowSorter(true);
        
        // Customize table header
        JTableHeader header = bookTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Add table sorter for filtering
        bookTableSorter = new TableRowSorter<>(bookTableModel);
        bookTable.setRowSorter(bookTableSorter);
        
        // Create scroll pane with custom styling
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        bookScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        bookScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Create top panel with search functionality
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        // Create search field with icon and placeholder
        JPanel searchFieldPanel = new JPanel(new BorderLayout());
        searchFieldPanel.setBackground(Color.WHITE);
        searchFieldPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setForeground(new Color(150, 150, 150));
        
        bookSearchField = new JTextField(20);
        bookSearchField.setBorder(null);
        bookSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookSearchField.setForeground(TEXT_COLOR);
        
        // Add placeholder text and listener
        bookSearchField.setText("Search books...");
        bookSearchField.setForeground(Color.GRAY);
        
        bookSearchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (bookSearchField.getText().equals("Search books...")) {
                    bookSearchField.setText("");
                    bookSearchField.setForeground(TEXT_COLOR);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (bookSearchField.getText().isEmpty()) {
                    bookSearchField.setText("Search books...");
                    bookSearchField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Add real-time search functionality
        bookSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterBooks();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterBooks();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterBooks();
            }
            
            private void filterBooks() {
                String text = bookSearchField.getText();
                if (text.equals("Search books...")) {
                    bookTableSorter.setRowFilter(null);
                } else {
                    bookTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        searchFieldPanel.add(searchIcon, BorderLayout.WEST);
        searchFieldPanel.add(bookSearchField, BorderLayout.CENTER);
        
        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(BACKGROUND_COLOR);
        
        // Add genre filter dropdown
        JLabel genreFilterLabel = new JLabel("Filter by Genre:");
        genreFilterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genreFilterLabel.setForeground(TEXT_COLOR);
        
        String[] genres = {"All Genres", "Classic", "Fiction", "Non-Fiction", "Fantasy", "Sci-Fi", "Mystery", "Biography", "History", "Dystopian"};
        bookGenreComboBox = new JComboBox<>(genres);
        bookGenreComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookGenreComboBox.setBackground(Color.WHITE);
        bookGenreComboBox.setForeground(TEXT_COLOR);
        
        bookGenreComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGenre = (String) bookGenreComboBox.getSelectedItem();
                if (selectedGenre.equals("All Genres")) {
                    bookTableSorter.setRowFilter(null);
                } else {
                    bookTableSorter.setRowFilter(RowFilter.regexFilter(selectedGenre, 3)); // Filter on genre column (index 3)
                }
            }
        });
        
        filterPanel.add(genreFilterLabel);
        filterPanel.add(bookGenreComboBox);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setBackground(SECONDARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBookTable();
                bookSearchField.setText("Search books...");
                bookSearchField.setForeground(Color.GRAY);
                bookGenreComboBox.setSelectedIndex(0);
                updateStatusMessage("Book list refreshed");
            }
        });
        
        filterPanel.add(refreshButton);
        
        searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        // Create form panel with improved layout
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(BACKGROUND_COLOR);
        formContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JPanel bookFormPanel = new JPanel(new GridBagLayout());
        bookFormPanel.setBackground(Color.WHITE);
        bookFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Add New Book", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14), PRIMARY_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create styled form fields
        bookIdField = createStyledTextField(10);
        bookTitleField = createStyledTextField(20);
        bookAuthorField = createStyledTextField(20);
        bookGenreField = createStyledTextField(10);
        
        // Add form labels with styling
        JLabel idLabel = createStyledLabel("Book ID:");
        JLabel titleLabel = createStyledLabel("Title:");
        JLabel authorLabel = createStyledLabel("Author:");
        JLabel genreLabel = createStyledLabel("Genre:");
        
        // Add components to form with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        bookFormPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        bookFormPanel.add(bookIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        bookFormPanel.add(titleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        bookFormPanel.add(bookTitleField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        bookFormPanel.add(authorLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        bookFormPanel.add(bookAuthorField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        bookFormPanel.add(genreLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        bookFormPanel.add(bookGenreField, gbc);
        
        // Create button panel with styled buttons
        JPanel bookButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookButtonPanel.setBackground(Color.WHITE);
        
        JButton clearButton = createStyledButton("Clear", SECONDARY_COLOR);
        JButton addBookButton = createStyledButton("Add Book", PRIMARY_COLOR);
        JButton removeBookButton = createStyledButton("Remove Selected", new Color(211, 47, 47)); // Material Red
        
        bookButtonPanel.add(clearButton);
        bookButtonPanel.add(addBookButton);
        bookButtonPanel.add(removeBookButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        bookFormPanel.add(bookButtonPanel, gbc);
        
        // Create a panel for book statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create stat cards
        JPanel totalBooksPanel = createStatCard("Total Books", "0", PRIMARY_COLOR);
        JPanel availableBooksPanel = createStatCard("Available Books", "0", new Color(76, 175, 80)); // Material Green
        JPanel checkedOutPanel = createStatCard("Checked Out", "0", new Color(255, 152, 0)); // Material Orange
        
        statsPanel.add(totalBooksPanel);
        statsPanel.add(availableBooksPanel);
        statsPanel.add(checkedOutPanel);
        
        // Add all panels to form container
        JPanel formAndStatsPanel = new JPanel(new BorderLayout());
        formAndStatsPanel.setBackground(BACKGROUND_COLOR);
        formAndStatsPanel.add(bookFormPanel, BorderLayout.CENTER);
        formAndStatsPanel.add(statsPanel, BorderLayout.SOUTH);
        
        formContainer.add(formAndStatsPanel, BorderLayout.CENTER);
        
        // Add all components to the book panel
        bookPanel.add(topPanel, BorderLayout.NORTH);
        bookPanel.add(bookScrollPane, BorderLayout.CENTER);
        bookPanel.add(formContainer, BorderLayout.SOUTH);
        
        // Add event listeners
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBookFields();
            }
        });
        
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
                updateBookStats();
            }
        });
        
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
                updateBookStats();
            }
        });
        
        // Add keyboard shortcuts
        bookTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    removeBook();
                    updateBookStats();
                }
            }
        });
        
        // Initialize book table and stats
        refreshBookTable();
        updateBookStats();
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
        return button;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(TEXT_COLOR);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        // Store the value label in a client property for later updates
        panel.putClientProperty("valueLabel", valueLabel);
        
        return panel;
    }
    
    private void updateBookStats() {
        try {
            // Get the form container panel (index 2 in bookPanel)
            JPanel formContainer = (JPanel) bookPanel.getComponent(2);
            // Get the formAndStatsPanel (index 0 in formContainer)
            JPanel formAndStatsPanel = (JPanel) formContainer.getComponent(0);
            // Get the statsPanel (index 1 in formAndStatsPanel)
            JPanel statsPanel = (JPanel) formAndStatsPanel.getComponent(1);
            // Get the stat cards from statsPanel
            Component[] components = statsPanel.getComponents();
            
            if (components.length >= 3) {
                JPanel totalBooksPanel = (JPanel) components[0];
                JPanel availableBooksPanel = (JPanel) components[1];
                JPanel checkedOutPanel = (JPanel) components[2];
                
                // Get the value labels
                JLabel totalBooksLabel = (JLabel) totalBooksPanel.getClientProperty("valueLabel");
                JLabel availableBooksLabel = (JLabel) availableBooksPanel.getClientProperty("valueLabel");
                JLabel checkedOutLabel = (JLabel) checkedOutPanel.getClientProperty("valueLabel");
                
                // Calculate statistics
                int totalBooks = library.getBooks().size();
                int availableBooks = 0;
            
                for (Book book : library.getBooks()) {
                    if (book.isAvailable()) {
                        availableBooks++;
                    }
                }
                
                int checkedOut = totalBooks - availableBooks;
                
                // Update the labels
                totalBooksLabel.setText(String.valueOf(totalBooks));
                availableBooksLabel.setText(String.valueOf(availableBooks));
                checkedOutLabel.setText(String.valueOf(checkedOut));
            }
        } catch (Exception e) {
            System.err.println("Error updating book stats: " + e.getMessage());
        }
    }
    
    private void createMemberPanel() {
        memberPanel = new JPanel(new BorderLayout(10, 10));
        memberPanel.setBackground(BACKGROUND_COLOR);
        memberPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create member table with enhanced styling
        String[] memberColumns = {"ID", "Name", "Email", "Phone", "Books Borrowed"};
        memberTableModel = new DefaultTableModel(memberColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        memberTable = new JTable(memberTableModel);
        memberTable.setRowHeight(30);
        memberTable.setIntercellSpacing(new Dimension(10, 5));
        memberTable.setGridColor(new Color(230, 230, 230));
        memberTable.setSelectionBackground(SECONDARY_COLOR);
        memberTable.setSelectionForeground(Color.WHITE);
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        memberTable.setAutoCreateRowSorter(true);
        
        // Customize table header
        JTableHeader header = memberTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Add table sorter for filtering
        memberTableSorter = new TableRowSorter<>(memberTableModel);
        memberTable.setRowSorter(memberTableSorter);
        
        // Create scroll pane with custom styling
        JScrollPane memberScrollPane = new JScrollPane(memberTable);
        memberScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        memberScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Create top panel with search functionality
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        // Create search field with icon and placeholder
        JPanel searchFieldPanel = new JPanel(new BorderLayout());
        searchFieldPanel.setBackground(Color.WHITE);
        searchFieldPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setForeground(new Color(150, 150, 150));
        
        memberSearchField = new JTextField(20);
        memberSearchField.setBorder(null);
        memberSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        memberSearchField.setForeground(TEXT_COLOR);
        
        // Add placeholder text and listener
        memberSearchField.setText("Search members...");
        memberSearchField.setForeground(Color.GRAY);
        
        memberSearchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (memberSearchField.getText().equals("Search members...")) {
                    memberSearchField.setText("");
                    memberSearchField.setForeground(TEXT_COLOR);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (memberSearchField.getText().isEmpty()) {
                    memberSearchField.setText("Search members...");
                    memberSearchField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Add real-time search functionality
        memberSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterMembers();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterMembers();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterMembers();
            }
            
            private void filterMembers() {
                String text = memberSearchField.getText();
                if (text.equals("Search members...")) {
                    memberTableSorter.setRowFilter(null);
                } else {
                    memberTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        searchFieldPanel.add(searchIcon, BorderLayout.WEST);
        searchFieldPanel.add(memberSearchField, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setBackground(SECONDARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshMemberTable();
                memberSearchField.setText("Search members...");
                memberSearchField.setForeground(Color.GRAY);
                updateStatusMessage("Member list refreshed");
            }
        });
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(BACKGROUND_COLOR);
        filterPanel.add(refreshButton);
        
        searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        // Create form panel with improved layout
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(BACKGROUND_COLOR);
        formContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JPanel memberFormPanel = new JPanel(new GridBagLayout());
        memberFormPanel.setBackground(Color.WHITE);
        memberFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Add New Member", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14), PRIMARY_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create styled form fields
        memberIdField = createStyledTextField(10);
        memberNameField = createStyledTextField(20);
        memberEmailField = createStyledTextField(20);
        memberPhoneField = createStyledTextField(10);
        
        // Add form labels with styling
        JLabel idLabel = createStyledLabel("Member ID:");
        JLabel nameLabel = createStyledLabel("Name:");
        JLabel emailLabel = createStyledLabel("Email:");
        JLabel phoneLabel = createStyledLabel("Phone:");
        
        // Add components to form with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        memberFormPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        memberFormPanel.add(memberIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        memberFormPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        memberFormPanel.add(memberNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        memberFormPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        memberFormPanel.add(memberEmailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        memberFormPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        memberFormPanel.add(memberPhoneField, gbc);
        
        // Create button panel with styled buttons
        JPanel memberButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        memberButtonPanel.setBackground(Color.WHITE);
        
        JButton clearButton = createStyledButton("Clear", SECONDARY_COLOR);
        JButton addMemberButton = createStyledButton("Add Member", PRIMARY_COLOR);
        JButton removeMemberButton = createStyledButton("Remove Selected", new Color(211, 47, 47)); // Material Red
        
        memberButtonPanel.add(clearButton);
        memberButtonPanel.add(addMemberButton);
        memberButtonPanel.add(removeMemberButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        memberFormPanel.add(memberButtonPanel, gbc);
        
        // Create a panel for member statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create stat cards
        JPanel totalMembersPanel = createStatCard("Total Members", "0", PRIMARY_COLOR);
        JPanel activeBorrowersPanel = createStatCard("Active Borrowers", "0", new Color(76, 175, 80)); // Material Green
        
        statsPanel.add(totalMembersPanel);
        statsPanel.add(activeBorrowersPanel);
        
        // Add all panels to form container
        JPanel formAndStatsPanel = new JPanel(new BorderLayout());
        formAndStatsPanel.setBackground(BACKGROUND_COLOR);
        formAndStatsPanel.add(memberFormPanel, BorderLayout.CENTER);
        formAndStatsPanel.add(statsPanel, BorderLayout.SOUTH);
        
        formContainer.add(formAndStatsPanel, BorderLayout.CENTER);
        
        // Add all components to the member panel
        memberPanel.add(topPanel, BorderLayout.NORTH);
        memberPanel.add(memberScrollPane, BorderLayout.CENTER);
        memberPanel.add(formContainer, BorderLayout.SOUTH);
        
        // Add event listeners
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMemberFields();
            }
        });
        
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
                updateMemberStats();
            }
        });
        
        removeMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeMember();
                updateMemberStats();
            }
        });
        
        // Add keyboard shortcuts
        memberTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    removeMember();
                    updateMemberStats();
                }
            }
        });
        
        // Initialize member table and stats
        refreshMemberTable();
        updateMemberStats();
    }
    
    private void updateMemberStats() {
        try {
            // Get the form container panel (index 2 in memberPanel)
            JPanel formContainer = (JPanel) memberPanel.getComponent(2);
            // Get the formAndStatsPanel (index 0 in formContainer)
            JPanel formAndStatsPanel = (JPanel) formContainer.getComponent(0);
            // Get the statsPanel (index 1 in formAndStatsPanel)
            JPanel statsPanel = (JPanel) formAndStatsPanel.getComponent(1);
            // Get the stat cards from statsPanel
            Component[] components = statsPanel.getComponents();
            
            if (components.length >= 2) {
                JPanel totalMembersPanel = (JPanel) components[0];
                JPanel activeBorrowersPanel = (JPanel) components[1];
                
                // Get the value labels
                JLabel totalMembersLabel = (JLabel) totalMembersPanel.getClientProperty("valueLabel");
                JLabel activeBorrowersLabel = (JLabel) activeBorrowersPanel.getClientProperty("valueLabel");
            
                // Calculate statistics
                int totalMembers = library.getMembers().size();
                int activeBorrowers = 0;
                
                for (Member member : library.getMembers()) {
                    if (!member.getBorrowedBooks().isEmpty()) {
                        activeBorrowers++;
                    }
                }
                
                // Update the labels
                totalMembersLabel.setText(String.valueOf(totalMembers));
                activeBorrowersLabel.setText(String.valueOf(activeBorrowers));
            }
        } catch (Exception e) {
            System.err.println("Error updating member stats: " + e.getMessage());
        }
    }
    
    private void createTransactionPanel() {
        transactionPanel = new JPanel(new BorderLayout(10, 10));
        transactionPanel.setBackground(BACKGROUND_COLOR);
        transactionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create transaction table with enhanced styling
        String[] transactionColumns = {"Book", "Member", "Type", "Timestamp"};
        transactionTableModel = new DefaultTableModel(transactionColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setRowHeight(30);
        transactionTable.setIntercellSpacing(new Dimension(10, 5));
        transactionTable.setGridColor(new Color(230, 230, 230));
        transactionTable.setSelectionBackground(SECONDARY_COLOR);
        transactionTable.setSelectionForeground(Color.WHITE);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transactionTable.setAutoCreateRowSorter(true);
        
        // Customize table header
        JTableHeader header = transactionTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Add table sorter for filtering
        transactionTableSorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(transactionTableSorter);
        
        // Create scroll pane with custom styling
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        transactionScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        transactionScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Create top panel with search functionality
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        // Create search field with icon and placeholder
        JPanel searchFieldPanel = new JPanel(new BorderLayout());
        searchFieldPanel.setBackground(Color.WHITE);
        searchFieldPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchIcon.setForeground(new Color(150, 150, 150));
        
        transactionSearchField = new JTextField(20);
        transactionSearchField.setBorder(null);
        transactionSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transactionSearchField.setForeground(TEXT_COLOR);
        
        // Add placeholder text and listener
        transactionSearchField.setText("Search transactions...");
        transactionSearchField.setForeground(Color.GRAY);
        
        transactionSearchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (transactionSearchField.getText().equals("Search transactions...")) {
                    transactionSearchField.setText("");
                    transactionSearchField.setForeground(TEXT_COLOR);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (transactionSearchField.getText().isEmpty()) {
                    transactionSearchField.setText("Search transactions...");
                    transactionSearchField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Add real-time search functionality
        transactionSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTransactions();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTransactions();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTransactions();
            }
            
            private void filterTransactions() {
                String text = transactionSearchField.getText();
                if (text.equals("Search transactions...")) {
                    transactionTableSorter.setRowFilter(null);
                } else {
                    transactionTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        searchFieldPanel.add(searchIcon, BorderLayout.WEST);
        searchFieldPanel.add(transactionSearchField, BorderLayout.CENTER);
        
        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(BACKGROUND_COLOR);
        
        // Add transaction type filter
        JLabel typeFilterLabel = new JLabel("Filter by Type:");
        typeFilterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeFilterLabel.setForeground(TEXT_COLOR);
        
        String[] types = {"All Types", "CHECKOUT", "RETURN"};
        transactionTypeComboBox = new JComboBox<>(types);
        transactionTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transactionTypeComboBox.setBackground(Color.WHITE);
        transactionTypeComboBox.setForeground(TEXT_COLOR);
        
        transactionTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) transactionTypeComboBox.getSelectedItem();
                if (selectedType.equals("All Types")) {
                    transactionTableSorter.setRowFilter(null);
                } else {
                    transactionTableSorter.setRowFilter(RowFilter.regexFilter(selectedType, 2)); // Filter on type column (index 2)
                }
            }
        });
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setBackground(SECONDARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTransactionTable();
                transactionSearchField.setText("Search transactions...");
                transactionSearchField.setForeground(Color.GRAY);
                transactionTypeComboBox.setSelectedIndex(0);
                updateStatusMessage("Transaction list refreshed");
            }
        });
        
        filterPanel.add(typeFilterLabel);
        filterPanel.add(transactionTypeComboBox);
        filterPanel.add(refreshButton);
        
        searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        // Create form panel with improved layout
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(BACKGROUND_COLOR);
        formContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JPanel transactionFormPanel = new JPanel(new GridBagLayout());
        transactionFormPanel.setBackground(Color.WHITE);
        transactionFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Book Transactions", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14), PRIMARY_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create styled combo boxes
        memberComboBox = new JComboBox<>();
        memberComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        memberComboBox.setBackground(Color.WHITE);
        memberComboBox.setPreferredSize(new Dimension(memberComboBox.getPreferredSize().width, 35));
        
        bookComboBox = new JComboBox<>();
        bookComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookComboBox.setBackground(Color.WHITE);
        bookComboBox.setPreferredSize(new Dimension(bookComboBox.getPreferredSize().width, 35));
        
        updateComboBoxes();
        
        // Add form labels with styling
        JLabel memberLabel = createStyledLabel("Member:");
        JLabel bookLabel = createStyledLabel("Book:");
        
        // Add components to form with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        transactionFormPanel.add(memberLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        transactionFormPanel.add(memberComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        transactionFormPanel.add(bookLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        transactionFormPanel.add(bookComboBox, gbc);
        
        // Create button panel with styled buttons
        JPanel transactionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        transactionButtonPanel.setBackground(Color.WHITE);
        
        JButton checkoutButton = createStyledButton("Checkout Book", PRIMARY_COLOR);
        JButton returnButton = createStyledButton("Return Book", new Color(76, 175, 80)); // Material Green
        
        transactionButtonPanel.add(checkoutButton);
        transactionButtonPanel.add(returnButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        transactionFormPanel.add(transactionButtonPanel, gbc);
        
        // Create a panel for transaction statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create stat cards
        JPanel checkoutsPanel = createStatCard("Total Checkouts", "0", PRIMARY_COLOR);
        JPanel returnsPanel = createStatCard("Total Returns", "0", new Color(76, 175, 80)); // Material Green
        
        statsPanel.add(checkoutsPanel);
        statsPanel.add(returnsPanel);
        
        // Add all panels to form container
        JPanel formAndStatsPanel = new JPanel(new BorderLayout());
        formAndStatsPanel.setBackground(BACKGROUND_COLOR);
        formAndStatsPanel.add(transactionFormPanel, BorderLayout.CENTER);
        formAndStatsPanel.add(statsPanel, BorderLayout.SOUTH);
        
        formContainer.add(formAndStatsPanel, BorderLayout.CENTER);
        
        // Add all components to the transaction panel
        transactionPanel.add(topPanel, BorderLayout.NORTH);
        transactionPanel.add(transactionScrollPane, BorderLayout.CENTER);
        transactionPanel.add(formContainer, BorderLayout.SOUTH);
        
        // Add event listeners
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutBook();
                updateTransactionStats();
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
                updateTransactionStats();
            }
        });
        
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() == transactionPanel) {
                    updateComboBoxes();
                    refreshTransactionTable();
                    updateTransactionStats();
                    updateStatusMessage("Transaction panel selected");
                }
            }
        });
        
        // Initialize transaction table and stats
        refreshTransactionTable();
        updateTransactionStats();
    }
    
    private void updateTransactionStats() {
        try {
            // Get the form container panel (index 2 in transactionPanel)
            JPanel formContainer = (JPanel) transactionPanel.getComponent(2);
            // Get the formAndStatsPanel (index 0 in formContainer)
            JPanel formAndStatsPanel = (JPanel) formContainer.getComponent(0);
            // Get the statsPanel (index 1 in formAndStatsPanel)
            JPanel statsPanel = (JPanel) formAndStatsPanel.getComponent(1);
            // Get the stat cards from statsPanel
            Component[] components = statsPanel.getComponents();
            
            if (components.length >= 2) {
                JPanel checkoutsPanel = (JPanel) components[0];
                JPanel returnsPanel = (JPanel) components[1];
                
                // Get the value labels
                JLabel checkoutsLabel = (JLabel) checkoutsPanel.getClientProperty("valueLabel");
                JLabel returnsLabel = (JLabel) returnsPanel.getClientProperty("valueLabel");
                
                // Calculate statistics
                int checkouts = 0;
                int returns = 0;
                
                for (Transaction transaction : library.getTransactions()) {
                    if (transaction.getType() == Transaction.Type.CHECKOUT) {
                        checkouts++;
                    } else if (transaction.getType() == Transaction.Type.RETURN) {
                        returns++;
                    }
                }
                
                // Update the labels
                checkoutsLabel.setText(String.valueOf(checkouts));
                returnsLabel.setText(String.valueOf(returns));
            }
        } catch (Exception e) {
            System.err.println("Error updating transaction stats: " + e.getMessage());
        }
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
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LibraryManagementSystemGUI().setVisible(true);
        });
    }
}