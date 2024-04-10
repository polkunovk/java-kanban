import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен " + PORT);
        setupHandlers();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }

    private void setupHandlers() {
        handleTasks();
        handleSubtasks();
        handleEpics();
        handleHistory();
        handlePrioritized();
    }

    private void handleTasks() {
        server.createContext("/tasks", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetTasks(exchange);
                } else if ("POST".equals(exchange.getRequestMethod())) {
                    handleAddTask(exchange);
                } else if ("DELETE".equals(exchange.getRequestMethod())) {
                    handleDeleteTask(exchange);
                }
            }
        });
    }

    private void handleSubtasks() {
        server.createContext("/subtasks", new HttpHandler() {
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
        });
    }

    private void handleEpics() {
        server.createContext("/epics", new HttpHandler() {
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
        });
    }

    private void handleHistory() {
        server.createContext("/history", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetHistory(exchange);
                }
            }
        });
    }

    private void handlePrioritized() {
        server.createContext("/prioritized", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetPrioritizedTasks(exchange);
                }
            }
        });
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String response = TaskSerializer.serializeTasks(tasks);
        sendResponse(exchange, 200, "Задачи получены", response);
    }

    private void handleAddTask(HttpExchange exchange) throws IOException {
        String requestBody = Utils.getRequestBody(exchange);
        Task task = TaskDeserializer.deserializeTask(requestBody);
        taskManager.addTask(task);
        sendResponse(exchange, 201, "Задача добавлена", "");
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String taskIdString = exchange.getRequestURI().getPath().split("/")[2];
        int taskId = Integer.parseInt(taskIdString);
        taskManager.deleteTask(taskId);
        sendResponse(exchange, 200, "Задача удалена", "");
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        String response = SubtaskSerializer.serializeSubtasks(subtasks);
        sendResponse(exchange, 200, "Подзадачи получены", response);
    }

    private void handleAddSubtask(HttpExchange exchange) throws IOException {
        String requestBody = Utils.getRequestBody(exchange);
        List<Subtask> newSubtasks = SubtaskDeserializer.deserializeSubtasks(requestBody);
        for (Subtask subtask : newSubtasks) {
            taskManager.createSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
        }
        sendResponse(exchange, 201, "Подзадачи добавлены", "");
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String subtaskIdString = exchange.getRequestURI().getPath().split("/")[2];
        int subtaskId = Integer.parseInt(subtaskIdString);
        taskManager.deleteSubtask(subtaskId);
        sendResponse(exchange, 200, "Подзадача удалена", "");
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String response = EpicSerializer.serializeEpics(epics);
        sendResponse(exchange, 200, "Эпики получены", response);
    }

    private void handleAddEpic(HttpExchange exchange) throws IOException {
        String requestBody = Utils.getRequestBody(exchange);
        Epic epic = EpicDeserializer.deserializeEpic(requestBody);
        taskManager.createEpic(epic.getTitle(), epic.getDescription());
        sendResponse(exchange, 201, "Эпик добавлен", "");
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String epicIdString = exchange.getRequestURI().getPath().split("/")[2];
        int epicId = Integer.parseInt(epicIdString);
        taskManager.deleteEpic(epicId);
        sendResponse(exchange, 200, "Эпик удален", "");
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String response = TaskSerializer.serializeTasks(history);
        sendResponse(exchange, 200, "История получена", response);
    }

    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        String response = TaskSerializer.serializeTasks(prioritizedTasks);
        sendResponse(exchange, 200, "Приоритетные задачи получены", response);
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
        exchange.getResponseHeaders().set("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(message);
    }

    public static void main(String[] args) {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }
}
