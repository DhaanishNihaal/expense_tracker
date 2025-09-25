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
    private static final String ADD_CATEGORY = "INSERT INTO categories (category_name) VALUES (?)";
    private static final String FILTER_NAMES = "SELECT category_name FROM categories";
    private static final String GET_CATEGORY_ID = "SELECT category_id FROM categories WHERE category_name = ?";
    private static final String ADD_EXPENSE = "INSERT INTO expenses (expense_name,description,amount, category_id, expense_date) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_CATEGORY_NAME = "SELECT category_name FROM categories WHERE category_id = ?";

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
}
