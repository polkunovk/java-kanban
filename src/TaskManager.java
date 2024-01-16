import java.util.*;

public class TaskManager {
    private static int idCounter = 1;
    private final Map<Integer, Task> allTasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;

    public TaskManager() {
        this.allTasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    public List<Task> getAllGeneralTasks() {
        List<Task> generalTasks = new ArrayList<>();
        for (Task task : allTasks.values()) {
            if (!(task instanceof Epic) && !(task instanceof Subtask)) {
                generalTasks.add(task);
            }
        }
        return generalTasks;
    }

    public List<Epic> getAllEpics() {
        List<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>(subtasks.values());
        return subtaskList;
    }

    public Task getTaskById(int taskId) {
        return allTasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void createEpic(String title, String description) {
        Epic epic = new Epic(title, description);
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(String title, String description, int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            Subtask subtask = new Subtask(title, description, epicId);
            subtask.setId(idCounter++);
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    public void updateTaskStatus(int taskId, TaskStatus status) {
        Task task = allTasks.get(taskId);
        if (task != null) {
            task.setStatus(status);
            if (task instanceof Subtask) {
                updateEpicStatus(((Subtask) task).getEpicId());
            }
        }
    }

    public List<Subtask> getAllSubtasksOfEpic(int epicId) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epicSubtasks.addAll(epic.getSubtasks());
        }
        return epicSubtasks;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.updateStatus();
            updateTask(epic);
        }
    }

    private void updateTask(Task updatedTask) {
        allTasks.put(updatedTask.getId(), updatedTask);
    }

    // Добавленный метод для создания общей задачи
    public void createTask(String title, String description) {
        Task task = new Task(title, description);
        task.setId(idCounter++);
        allTasks.put(task.getId(), task);
    }

    // Добавленный метод для обновления статуса эпика
    public void updateEpic(int epicId, TaskStatus status) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.setStatus(status);
            updateTask(epic);
        }
    }

    // Добавленный метод для обновления статуса подзадачи
    public void updateSubTask(int subtaskId, TaskStatus status) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            subtask.setStatus(status);
            updateEpicStatus(subtask.getEpicId());
            updateTask(subtask);
        }
    }
}
