# 💼 Expense Tracker - Professional Java Application

A comprehensive expense management application built with Java Swing, featuring a modern GUI interface, data persistence, and advanced filtering capabilities. Perfect for resume portfolios and interview demonstrations.

## ✨ Features

### 🎯 **Core Functionality**
- **Add Expenses**: Input amount, category, and description
- **View Expenses**: Professional table display with formatted amounts
- **Delete Expenses**: Remove selected entries with confirmation
- **Data Persistence**: Save/load expenses to/from CSV file
- **Auto-save**: Automatically saves data after each addition/deletion

### 🔍 **Advanced Features**
- **Category Management**: Predefined categories + custom categories
- **Filtering System**: Filter by category and search descriptions
- **Real-time Totals**: Automatic calculation and display of total expenses
- **Professional UI**: Modern Nimbus look and feel with color-coded buttons
- **Menu Bar**: File operations and help menu
- **Input Validation**: Comprehensive error checking and user feedback

### 💾 **Data Management**
- **CSV Format**: Human-readable data storage
- **Robust Parsing**: Handles CSV escaping and special characters
- **Error Handling**: Graceful handling of file operations
- **Backup Safety**: Confirmation dialogs for destructive operations

## 🚀 How to Run

1. **Compile**: `javac ExpenseTracker.java`
2. **Run**: `java -cp . ExpenseTracker`

## 📱 User Interface

### Main Window Components:
- **Input Form**: Amount, category dropdown, description field
- **Expense Table**: Read-only table with formatted display
- **Filter Panel**: Category and description filtering
- **Action Buttons**: Add, delete, save, load operations
- **Status Display**: Real-time total expense calculation

### Menu Options:
- **File Menu**: Save, Load, Exit (with auto-save)
- **Help Menu**: About dialog

## 🏗️ Technical Implementation

### **Architecture**
- **MVC Pattern**: Separation of data, view, and control logic
- **Model Class**: `Expense` with proper encapsulation
- **Swing Components**: Professional GUI with event handling
- **File I/O**: Custom CSV parser with escaping support

### **Advanced Java Concepts**
- **Swing GUI Framework**: JFrame, JTable, JButton, JComboBox
- **Event-Driven Programming**: Action listeners and event handling
- **Data Structures**: ArrayList for expense storage
- **File Processing**: BufferedReader/PrintWriter for CSV operations
- **Exception Handling**: Try-catch blocks with user feedback
- **Date/Time API**: LocalDate for expense timestamps

### **Code Quality**
- **Input Validation**: Comprehensive error checking
- **User Experience**: Confirmation dialogs and status messages
- **Memory Management**: Efficient data structures
- **Thread Safety**: SwingUtilities for EDT compliance

## 📊 Sample Usage

1. **Add Expense**: Enter amount ($50.00), select category (Food), add description (Lunch at restaurant)
2. **View Data**: See formatted table with date, category, description, amount
3. **Filter**: Use category filter or search descriptions
4. **Save Data**: Automatically saved to `expenses.csv`
5. **Load Data**: Reload previously saved expenses

## 🎯 Interview Highlights

This project demonstrates:
- **Professional Java Development**: Real-world application design
- **GUI Programming**: Advanced Swing component usage
- **Data Persistence**: File I/O and data management
- **User Experience**: Intuitive interface design
- **Error Handling**: Robust application behavior
- **Code Organization**: Clean, maintainable structure

## 📋 Requirements

- **Java Version**: JDK 8 or higher
- **Dependencies**: None (uses standard Java libraries)
- **Platform**: Cross-platform (Windows, macOS, Linux)

## 🔧 Customization

The application can be easily extended with:
- Additional expense fields (payment method, tags)
- Charts and graphs for expense visualization
- Export to different formats (PDF, Excel)
- User authentication and multi-user support
- Cloud synchronization

Perfect for showcasing Java GUI development skills in job applications and technical interviews!