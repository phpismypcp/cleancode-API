package org.isep.cleancode.persistence.csvfiles;

import org.isep.cleancode.Todo;
import org.isep.cleancode.application.port.ITodoRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TodoCsvFilesRepository implements ITodoRepository {
    private final String filePath;
    private final List<Todo> cachedTodos = new ArrayList<>();

    public TodoCsvFilesRepository() {
        // Create a file in the AppData directory
        String appDataPath = System.getenv("APPDATA");
        if (appDataPath == null) {
            // Fallback for non-Windows systems
            appDataPath = System.getProperty("user.home");
        }
        this.filePath = appDataPath + File.separator + "todos.csv";
        loadTodosFromFile();
    }

    @Override
    public void addTodo(Todo todo) {
        cachedTodos.add(todo);
        saveTodosToFile();
    }

    @Override
    public List<Todo> getAllTodos() {
        return new ArrayList<>(cachedTodos);
    }

    @Override
    public boolean existsByName(String name) {
        return cachedTodos.stream().anyMatch(todo -> todo.getName().equals(name));
    }

    private void loadTodosFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String name = parts[0];
                    LocalDate dueDate = null;

                    if (parts.length > 1 && !parts[1].isEmpty()) {
                        try {
                            dueDate = LocalDate.parse(parts[1]);
                        } catch (DateTimeParseException e) {
                            // Invalid date, ignore it
                        }
                    }

                    Todo todo = new Todo(name);
                    todo.setDueDate(dueDate);
                    cachedTodos.add(todo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTodosToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Todo todo : cachedTodos) {
                String line = todo.getName();
                if (todo.getDueDate() != null) {
                    line += "," + todo.getDueDate();
                } else {
                    line += ",";
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}