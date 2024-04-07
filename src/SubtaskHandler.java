import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;

public class SubtaskHandler implements HttpHandler {
    private TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetSubtasks(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handleAddSubtask(exchange);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            handleDeleteSubtask(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        String response = SubtaskSerializer.serializeSubtasks(subtasks);
        sendResponse(exchange, 200, "Подзадачи получены", response);
    }

    private void handleAddSubtask(HttpExchange exchange) throws IOException {
        String requestBody = Utils.getRequestBody(exchange);
        Subtask subtask = SubtaskDeserializer.deserializeSubtasks(requestBody).get(0); // Исправлено
        taskManager.createSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId()); // Исправлено
        sendResponse(exchange, 201, "Подзадача добавлена", ""); // Исправлено
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String subtaskIdString = exchange.getRequestURI().getPath().split("/")[2];
        int subtaskId = Integer.parseInt(subtaskIdString);
        taskManager.deleteSubtask(subtaskId);
        sendResponse(exchange, 200, "Подзадача удалена", ""); // Исправлено
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String message, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().set("Access-Control-Max-Age", "86400");
        exchange.getResponseHeaders().set("Cache-Control", "no-cache");
        exchange.getResponseHeaders().set("Pragma", "no-cache");
        exchange.getResponseHeaders().set("Expires", "-1");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}
