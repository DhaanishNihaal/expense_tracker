package com.expense.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.expense.model.Category;
import com.expense.model.Expense;
import com.expense.dao.ExpenseDAO;
import java.time.LocalDateTime;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class MainUI extends JFrame{
    private JButton category;
    private JButton expense;

    public MainUI(){
        intializeComponents();
        setupLayout();
        setUpListeners();

    }
    private void setUpListeners(){
        category.addActionListener(e -> {
            CategoryUI categoryUI = new CategoryUI();
            categoryUI.setVisible(true);
        });
        expense.addActionListener(e -> {
            ExpenseUI expenseUI = new ExpenseUI();
            expenseUI.setVisible(true);
        });
    }
    private void intializeComponents(){
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setLocationRelativeTo(null);
        category = new JButton("Category");
        expense = new JButton("Expense");
    }
    private void setupLayout() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(category);
        buttonPanel.add(expense);
        category.setPreferredSize(new Dimension(500,200));
        category.setFont(new Font("Arial",Font.BOLD,50));
        expense.setPreferredSize(new Dimension(500,200));
        expense.setFont(new Font("Arial",Font.BOLD,50));
    
        JPanel mainpanel = new JPanel(new GridBagLayout()); // centers child panel
        mainpanel.add(buttonPanel);
    
        add(mainpanel, BorderLayout.CENTER);
    }
    
}
class CategoryUI extends JFrame{

    private JButton addCategory;
    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JTextField categoryName;
    private DefaultTableModel expenseTableModel;
    private JTable expenseTable;
    private ExpenseDAO expenseDAO;




    public CategoryUI(){
        this.expenseDAO = new ExpenseDAO();
        intializeComponents();
        setupLayout();
        setUpEventListeners();
        loadCategory();

    }

    private void intializeComponents(){
        setTitle("Category Table");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        addCategory = new JButton("Add Category");
        
        // categoryTableModel = new DefaultTableModel();
        categoryName = new JTextField(20);
        String[] columns = {"ID","Name"};
        categoryTableModel = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // categoryTable.getSelectionModel().addListSelectionListener(
        //     (e)->{
        //         if(!categoryTable.getValueIsAdjusting()){
        //             loadSelectedCategory();
        //         }
        //     }
        // );

        
    }
    private void setupLayout(){
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx  = 0;
        gbc.gridy = 0;

        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("category name"),gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryName,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(addCategory,gbc);


        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);

        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(categoryTable),BorderLayout.CENTER);
        
    }
    
    private void setUpEventListeners(){
        addCategory.addActionListener((e)->{addCategory();});
    }
    private void loadCategory(){
        try{
            List<Category> categories =  expenseDAO.getAllcat();
            updateCategoryTable(categories);
        }
        catch(Exception e){
            e.printStackTrace(); // This will print the detailed SQL error to the console
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
            
    private void updateCategoryTable(List<Category> categories) {
        categoryTableModel.setRowCount(0);
        for (Category c : categories) {
            categoryTableModel.addRow(new Object[]{c.getId(), c.getName()});
        }
    }
    private void addCategory(){
        String name = categoryName.getText().trim();
        if(name.isEmpty()){
            JOptionPane.showMessageDialog(this, "Category name cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            expenseDAO.addCategory(name);
            loadCategory();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

class ExpenseUI extends JFrame {
    private ExpenseDAO expenseDAO;
    private JTable expenseTable;
    private DefaultTableModel expenseTableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> categoryComboBox;
    private JTextField amountField;
    private JSpinner dateSpinner;

    public ExpenseUI() {
        this.expenseDAO = new ExpenseDAO();
        intializeComponents();
        setupLayout();
        loadExpense();
        setUpListeners();
    }

    private void intializeComponents()
    {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(2, 10);
        amountField = new JTextField(20);
        addButton = new JButton("Add Expense");
        updateButton = new JButton("Update Expense");
        deleteButton = new JButton("Delete Expense");
        filterComboBox = new JComboBox<>(filteroptions(0));
        categoryComboBox = new JComboBox<>(filteroptions(1));



        String[] columns = {"ID","Name","Description","Amount","Category","Date"};
        expenseTableModel = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        expenseTable = new JTable(expenseTableModel);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel ("Title"),gbc);
        gbc.gridx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        inputPanel.add(new JLabel("Description"),gbc);
        gbc.gridx=1;
        inputPanel.add(new JScrollPane(descriptionArea),gbc);
        
        gbc.gridx=0;
        gbc.gridy=3;
        inputPanel.add(new JLabel("Select Category:"),gbc);
        gbc.gridx=1;
        inputPanel.add(categoryComboBox,gbc);

        gbc.gridx=0;
        gbc.gridy=4;
        inputPanel.add(new JLabel("Amount"),gbc);
        gbc.gridx=1;
        inputPanel.add(amountField,gbc);

        gbc.gridx=0;
        gbc.gridy=5;
        inputPanel.add(new JLabel("Date"),gbc);
        gbc.gridx=1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss"));
        inputPanel.add(dateSpinner,gbc);



        JPanel buttonPanel=new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel filterPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter Category:"));
        filterPanel.add(filterComboBox);



        JPanel northPanel=new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);
        northPanel.add(filterPanel,BorderLayout.NORTH);


        add(northPanel,BorderLayout.NORTH);

        add(new JScrollPane(expenseTable),BorderLayout.CENTER);
    }
    private void setUpListeners() {
        addButton.addActionListener(e -> addExpense());
        // updateButton.addActionListener(e -> updateExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        // filterComboBox.addActionListener(e -> filterExpenses());
    }
    
    private void loadExpense() {
        try{
            List<Expense> expenses = expenseDAO.getAllExpenses();
            updateExpenseTable(expenses);
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load expenses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateExpenseTable(List<Expense> expenses) {
        expenseTableModel.setRowCount(0);
        for (Expense expense : expenses) {
            try{
            expenseTableModel.addRow(new Object[]{
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getAmount(),
                expenseDAO.getCategoryName((expense.getCategoryId())),
                expense.getDate()
            });
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    }
    
    private String[] filteroptions(int type){
        try{
            List<String> list = expenseDAO.getAllcatnames();
            if (type == 0) {
                list.add(0, "All");
            }
            return list.toArray(new String[0]);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new String[]{"All"};
    }
    private void addExpense(){
        try{
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        double amount = Double.parseDouble(amountField.getText().trim());
        int categoryID= expenseDAO.getCategoryId((String) categoryComboBox.getSelectedItem());
        Date date = (Date) dateSpinner.getValue();

        if (title.isEmpty() ||  amount <= 0) {
            JOptionPane.showMessageDialog(this, "Please enter valid expense details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Expense expense = new Expense(title, description, amount,categoryID,date);
        expenseDAO.addExpense(expense);
        loadExpense();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void 
}

