import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Expense Tracker - Professional Java Application
 * Features: GUI interface, data persistence, categorization, filtering
 */
public class ExpenseTracker extends JFrame {

    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JTextField amountField, descriptionField;
    private JComboBox<String> categoryCombo;
    private JButton addButton, deleteButton, saveButton, loadButton, filterButton;
    private JLabel totalLabel;
    private List<Expense> expenses;
    private JComboBox<String> filterCategoryCombo;
    private JTextField filterDescriptionField;

    private static final String DATA_FILE = "expenses.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ExpenseTracker() {
        expenses = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadExpensesFromFile();
        updateTotal();

        setTitle("Expense Tracker - Professional Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"Date", "Category", "Description", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.getTableHeader().setReorderingAllowed(false);

        // Format amount column
        TableColumn amountColumn = expenseTable.getColumnModel().getColumn(3);
        amountColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Double) {
                    setText(String.format("$%.2f", (Double) value));
                }
                setHorizontalAlignment(JLabel.RIGHT);
                return this;
            }
        });

        // Input fields
        amountField = new JTextField(10);
        descriptionField = new JTextField(20);

        // Category combo box
        String[] defaultCategories = {"Food", "Transportation", "Entertainment", "Utilities",
                                   "Healthcare", "Shopping", "Education", "Other"};
        categoryCombo = new JComboBox<>(defaultCategories);
        categoryCombo.setEditable(true); // Allow custom categories

        // Buttons
        addButton = new JButton("Add Expense");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);

        deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);

        saveButton = new JButton("Save Data");
        saveButton.setBackground(new Color(52, 152, 219));
        saveButton.setForeground(Color.WHITE);

        loadButton = new JButton("Load Data");
        loadButton.setBackground(new Color(155, 89, 182));
        loadButton.setForeground(Color.WHITE);

        filterButton = new JButton("Apply Filter");

        // Filter components
        filterCategoryCombo = new JComboBox<>(new String[]{"All Categories"});
        filterCategoryCombo.setEditable(false);
        filterDescriptionField = new JTextField(15);
        filterDescriptionField.setToolTipText("Search in descriptions");

        // Total label
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(52, 73, 94));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        saveMenuItem.addActionListener(e -> saveExpensesToFile());
        loadMenuItem.addActionListener(e -> loadExpensesFromFile());
        exitMenuItem.addActionListener(e -> {
            saveExpensesToFile();
            System.exit(0);
        });

        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));
        inputPanel.add(new JLabel("Amount: $"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryCombo);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(addButton);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Expenses"));
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(filterCategoryCombo);
        filterPanel.add(new JLabel("Description:"));
        filterPanel.add(filterDescriptionField);
        filterPanel.add(filterButton);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Top panel combining input and filter
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Expenses"));

        // Bottom panel with total
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalLabel, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteSelectedExpense());
        saveButton.addActionListener(e -> saveExpensesToFile());
        loadButton.addActionListener(e -> loadExpensesFromFile());
        filterButton.addActionListener(e -> applyFilter());

        // Enter key support
        amountField.addActionListener(e -> addExpense());
        descriptionField.addActionListener(e -> addExpense());

        // Update filter categories when expenses change
        categoryCombo.addActionListener(e -> updateFilterCategories());
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive!",
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String category = (String) categoryCombo.getSelectedItem();
            String description = descriptionField.getText().trim();

            if (description.isEmpty()) {
                description = "No description";
            }

            Expense expense = new Expense(LocalDate.now(), category, description, amount);
            expenses.add(expense);

            // Add to table
            Object[] row = {
                expense.getDate().format(DATE_FORMATTER),
                expense.getCategory(),
                expense.getDescription(),
                expense.getAmount()
            };
            tableModel.addRow(row);

            // Clear input fields
            amountField.setText("");
            descriptionField.setText("");

            updateTotal();
            updateFilterCategories();

            // Auto-save
            saveExpensesToFile();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!",
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete!",
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this expense?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            expenses.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            updateTotal();
            saveExpensesToFile();
        }
    }

    private void applyFilter() {
        String filterCategory = (String) filterCategoryCombo.getSelectedItem();
        String filterText = filterDescriptionField.getText().toLowerCase();

        tableModel.setRowCount(0); // Clear table

        for (Expense expense : expenses) {
            boolean categoryMatch = "All Categories".equals(filterCategory) ||
                                  expense.getCategory().equals(filterCategory);
            boolean descriptionMatch = filterText.isEmpty() ||
                                     expense.getDescription().toLowerCase().contains(filterText);

            if (categoryMatch && descriptionMatch) {
                Object[] row = {
                    expense.getDate().format(DATE_FORMATTER),
                    expense.getCategory(),
                    expense.getDescription(),
                    expense.getAmount()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void updateFilterCategories() {
        Set<String> categories = new HashSet<>();
        categories.add("All Categories");
        for (Expense expense : expenses) {
            categories.add(expense.getCategory());
        }

        filterCategoryCombo.removeAllItems();
        for (String category : categories) {
            filterCategoryCombo.addItem(category);
        }
    }

    private void updateTotal() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void saveExpensesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println("Date,Category,Description,Amount"); // CSV header
            for (Expense expense : expenses) {
                writer.printf("%s,%s,%s,%.2f%n",
                    expense.getDate().format(DATE_FORMATTER),
                    escapeCSV(expense.getCategory()),
                    escapeCSV(expense.getDescription()),
                    expense.getAmount());
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully!",
                                        "Save Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(),
                                        "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExpensesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            expenses.clear();
            tableModel.setRowCount(0);

            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 4) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                        String category = unescapeCSV(parts[1]);
                        String description = unescapeCSV(parts[2]);
                        double amount = Double.parseDouble(parts[3]);

                        Expense expense = new Expense(date, category, description, amount);
                        expenses.add(expense);

                        Object[] row = {parts[0], category, description, amount};
                        tableModel.addRow(row);
                    } catch (Exception e) {
                        // Skip invalid lines
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }

            updateTotal();
            updateFilterCategories();
            JOptionPane.showMessageDialog(this, "Data loaded successfully!",
                                        "Load Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                                        "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCSV(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }

    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Expense Tracker v1.0\n\n" +
            "A professional expense management application\n" +
            "Features: GUI interface, data persistence, filtering\n\n" +
            "Built with Java Swing",
            "About Expense Tracker",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                // Use default look and feel
            }
            new ExpenseTracker();
        });
    }
}

/**
 * Expense model class
 */
class Expense {
    private LocalDate date;
    private String category;
    private String description;
    private double amount;

    public Expense(LocalDate date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    // Getters
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("Expense{date=%s, category='%s', description='%s', amount=%.2f}",
                           date, category, description, amount);
    }
}