package org.isep.cleancode.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.isep.cleancode.Todo;
import org.isep.cleancode.application.TodoManager;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class TodoController {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private final TodoManager todoManager;

    public TodoController(TodoManager todoManager) {
        this.todoManager = todoManager;
    }

    public Object getAllTodos(Request req, Response res) {
        res.type("application/json");
        return gson.toJson(todoManager.getAllTodos());
    }

    public Object createTodo(Request req, Response res) {
        res.type("application/json");

        Todo newTodo = gson.fromJson(req.body(), Todo.class);

        try {
            todoManager.createTodo(newTodo);
            res.status(201);
            return gson.toJson(newTodo);
        } catch (IllegalArgumentException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    // Helper class for error responses
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}

// Adapter for LocalDate serialization/deserialization
class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return date == null ? null : new JsonPrimitive(date.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return json.isJsonNull() ? null : LocalDate.parse(json.getAsString());
    }
}