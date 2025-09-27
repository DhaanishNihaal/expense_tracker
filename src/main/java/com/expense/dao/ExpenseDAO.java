package com.expense.dao;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

import com.expense.model.Category;
import com.expense.model.Expense;
import com.expense.util.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
public class ExpenseDAO {

    private static final String SELECT_ALL = "SELECT * FROM categories";
    private static final String SELECT_EXP = "SELECT * FROM expenses";
    private static final String SELECT_EXP_BY_ID = "SELECT * FROM expenses WHERE category_id = ?";
    private static final String ADD_CATEGORY = "INSERT INTO categories (category_name) VALUES (?)";
    private static final String FILTER_NAMES = "SELECT category_name FROM categories";
    private static final String GET_CATEGORY_ID = "SELECT category_id FROM categories WHERE category_name = ?";
    private static final String ADD_EXPENSE = "INSERT INTO expenses (expense_name,description,amount, category_id, expense_date) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_CATEGORY_NAME = "SELECT category_name FROM categories WHERE category_id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expenses WHERE expense_id = ?";
    private static final String UPDATE_EXPENSE = "UPDATE expenses SET expense_name = ?,description = ?, amount = ?, category_id = ?,expense_date = ? WHERE expense_id = ?";
    private static final String GET_TOTAL_AMOUNT = "SELECT SUM(amount) FROM expenses WHERE category_id = ?";
    private static final String GET_TOTAL_AMOUNT_ALL = "SELECT SUM(amount)  FROM expenses";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET category_name = ? WHERE category_id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE category_id = ?";
    private static final String DELETE_EXPENSES_BY_CATEGORY = "DELETE FROM expenses WHERE category_id = ?";
    private static final String COUNT_EXPENSES_BY_CATEGORY = "SELECT COUNT(*) FROM expenses WHERE category_id = ?";


    private Category getCategoryRow(ResultSet rs) throws SQLException{
        int id = rs.getInt("category_id");
        String catname = rs.getString("category_name");
        Category cat = new Category(id, catname);
        return cat;
    }

    private Expense getExpenseRow(ResultSet rs) throws SQLException
    {
        int id = rs.getInt("expense_id");
        String name = rs.getString("expense_name");
        String description = rs.getString("description");
        double amount = rs.getDouble("amount");
        int categoryId = rs.getInt("category_id");
        Date date = rs.getDate("expense_date");
        Expense exp = new Expense(id, name, description, amount, categoryId, date);
        return exp;
    }

    public List<Category> getAllcat() throws SQLException{
        List<Category> cats = new ArrayList<>();
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_ALL)) {
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    cats.add(getCategoryRow(rs));
                }
            }
        return cats;
    }


    public List<Expense> getAllExpenses() throws SQLException
    {
        List<Expense> exps = new ArrayList<>();
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_EXP)) {
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    exps.add(getExpenseRow(rs));
                }
            }
        return exps;
    }
    public List<Expense> getExpensesByCategory(int id) throws SQLException
    {
        List<Expense> exps = new ArrayList<>();
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_EXP_BY_ID)) {
                stmt.setInt(1,id);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    exps.add(getExpenseRow(rs));
                }
            }
        return exps;
    }
    public void addCategory( String name) throws SQLException{
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(ADD_CATEGORY)
        ){
            stmt.setString(1,name);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Creating category failed, no rows affected.");
            }
        }
    }
    public boolean updateCategory(int id,String name) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY);
        ){
            stmt.setString(1,name);
            stmt.setInt(2,id);
            int rows = stmt.executeUpdate();
            return rows>0;
        }
    }
    public boolean deleteCategory(int id) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement Stmt1 = conn.prepareStatement(DELETE_CATEGORY);
            PreparedStatement Stmt2 = conn.prepareStatement(DELETE_EXPENSES_BY_CATEGORY);
        ){
            Stmt1.setInt(1,id);            Stmt2.setInt(1,id);
            Stmt2.setInt(1,id);
            Stmt2.executeUpdate();
            int rows2=Stmt1.executeUpdate();
            return rows2>0;
        }
    }
        
    public List<String> getAllcatnames() throws SQLException{
        List<String> names = new ArrayList<>();
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(FILTER_NAMES)) {
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    names.add(rs.getString("category_name"));
                }
            }
        return names;
    }
    public int getCategoryId(String name) throws SQLException{
        int id = -1;
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_ID)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    id = rs.getInt("category_id");
                }
            }
        return id;
    }
    public String getCategoryName(int id) throws SQLException{
        String name = null;
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_NAME)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    name = rs.getString("category_name");
                }
            }
        return name;
    }
    public int getExpenseCountByCategory(int categoryId) throws SQLException{
        int count = 0;
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(COUNT_EXPENSES_BY_CATEGORY)) {
                stmt.setInt(1, categoryId);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    count = rs.getInt(1);
                }
            }
        return count;
    }
    public void addExpense(Expense expense) throws SQLException{
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(ADD_EXPENSE, Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1,expense.getName());
            stmt.setString(2,expense.getDescription());
            stmt.setDouble(3,expense.getAmount());
            stmt.setInt(4,expense.getCategoryId());
            stmt.setDate(5, new java.sql.Date(expense.getDate().getTime()));
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Creating expense failed, no rows affected.");
            }
        }
        
    }
    public void deleteExpense(int id) throws SQLException{
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_EXPENSE)
        ){
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Deleting expense failed, no rows affected.");
            }
        }
    }

    public boolean updateExpense(Expense expense) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_EXPENSE);
        ){
            stmt.setString(1,expense.getName());
            stmt.setString(2,expense.getDescription());
            stmt.setDouble(3,expense.getAmount());
            stmt.setInt(4,expense.getCategoryId());
            stmt.setDate(5, new java.sql.Date(expense.getDate().getTime()));
            stmt.setInt(6,expense.getId());
            int rows = stmt.executeUpdate();
            return rows>0;
        }

    }
    public double getTotalAmount(int catID) throws SQLException{
        double total = 0.0;
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_TOTAL_AMOUNT)) {
                stmt.setInt(1, catID);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    total = rs.getDouble(1);
                }
            }

        return total;
    }
    public double getTotalAmountAll() throws SQLException{
        double total = 0.0;
        try(
            Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_TOTAL_AMOUNT_ALL)) {
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    total = rs.getDouble(1);
                }
            }

        return total;
    }
}
