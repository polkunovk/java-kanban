import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;

public class EpicHandler implements HttpHandler {
    private TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetEpics(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handleAddEpic(exchange);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            handleDeleteEpic(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String response = EpicSerializer.serializeEpics(epics);
        sendResponse(exchange, 200, response);
    }

    private void handleAddEpic(HttpExchange exchange) throws IOException {
        String requestBody = Utils.getRequestBody(exchange);
        Epic epic = EpicDeserializer.deserializeEpic(requestBody);
        taskManager.createEpic(epic.getTitle(), epic.getDescription());
        sendResponse(exchange, 201, "Эпик добавлен");
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String epicIdString = exchange.getRequestURI().getPath().split("/")[2];
        int epicId = Integer.parseInt(epicIdString);
        taskManager.deleteEpic(epicId);
        sendResponse(exchange, 200, "Эпик удален");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
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
