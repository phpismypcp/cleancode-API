package org.isep.cleancode.application.port;

import org.isep.cleancode.Todo;
import java.util.List;

public interface ITodoRepository {
    void addTodo(Todo todo);
    List<Todo> getAllTodos();
    boolean existsByName(String name);
}