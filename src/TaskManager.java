import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TaskManager {
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

    public void createEpic(String title, String description) {
        Epic epic = new Epic(title, description);
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
        allTasks.put(epic.getId(), epic);
    }

    public void createSubtask(String title, String description, int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            Subtask subtask = new Subtask(title, description, epicId);
            subtask.setId(idCounter++);
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            updateEpicStatus(subtask.getEpicId());
            allTasks.put(subtask.getId(), subtask);
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
}
