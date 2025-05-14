package org.isep.cleancode;


import java.time.LocalDate;

public class Todo {
    private String name;
    private LocalDate dueDate;

    public Todo() {
        // Default constructor for JSON deserialization
    }

    public Todo(String name) {
        this.name = name;
    }

    public Todo(String name, LocalDate dueDate) {
        this.name = name;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Business rule validation
    public boolean isValid() {
        // Name is required and must be shorter than 64 characters
        return name != null && !name.isEmpty() && name.length() < 64;
    }
}