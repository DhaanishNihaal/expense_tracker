package com.expense.model;

import java.util.Date;

public class Expense {
    private int id;
    private String name;
    private String description;
    private double amount;
    private int categoryId;
    private Date date;

    public Expense(String name,String description, double amount, int categoryId, Date date) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
    }
    public Expense(int id, String name,String description, double amount, int categoryId, Date date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getAmount() {
        return amount;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public Date getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
