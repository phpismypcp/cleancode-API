package org.isep.cleancode.application;

import org.isep.cleancode.Todo;
import org.isep.cleancode.application.port.ITodoRepository;
import java.util.List;

public class TodoManager {
    private final ITodoRepository todoRepository;

    public TodoManager(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(Todo todo) throws IllegalArgumentException {
        // Validate business rules
        if (!todo.isValid()) {
            throw new IllegalArgumentException("Todo name is required and must be shorter than 64 characters");
        }

        // Check for duplicate names
        if (todoRepository.existsByName(todo.getName())) {
            throw new IllegalArgumentException("Todo with this name already exists");
        }

        todoRepository.addTodo(todo);
        return todo;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.getAllTodos();
    }
}