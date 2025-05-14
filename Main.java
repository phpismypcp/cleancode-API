package org.isep.cleancode;

import org.isep.cleancode.application.TodoManager;
import org.isep.cleancode.application.port.ITodoRepository;
import org.isep.cleancode.config.AppConfig;
import org.isep.cleancode.persistence.csvfiles.TodoCsvFilesRepository;
import org.isep.cleancode.persistence.inmemory.TodoInMemoryRepository;
import org.isep.cleancode.presentation.TodoController;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : null;
        AppConfig config = new AppConfig(configPath);

        // Select repository based on configuration
        ITodoRepository repository;
        if ("CSV".equalsIgnoreCase(config.getRepositoryType())) {
            System.out.println("Using CSV repository");
            repository = new TodoCsvFilesRepository();
        } else {
            System.out.println("Using in-memory repository");
            repository = new TodoInMemoryRepository();
        }

        // Create TodoManager with the selected repository
        TodoManager todoManager = new TodoManager(repository);

        // Create TodoController with the TodoManager
        TodoController todoController = new TodoController(todoManager);

        port(4567);
        get("/todos", todoController::getAllTodos);
        post("/todos", todoController::createTodo);
    }
}