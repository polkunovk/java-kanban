import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TaskManager {
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

    public void createTask(Task task) {
        allTasks.put(task.getId(), task);
        if (task instanceof Subtask) {
            subtasks.put(task.getId(), (Subtask) task);
        } else if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        }
    }

    public void updateTask(Task updatedTask) {
        Task existingTask = allTasks.get(updatedTask.getId());
        if (existingTask != null) {
            allTasks.put(updatedTask.getId(), updatedTask);

            if (updatedTask instanceof Subtask) {
                subtasks.put(updatedTask.getId(), (Subtask) updatedTask);
            } else if (updatedTask instanceof Epic) {
                epics.put(updatedTask.getId(), (Epic) updatedTask);
            }
        }
    }

    public List<Subtask> getAllSubtasksOfEpic(int epicId) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }
}
