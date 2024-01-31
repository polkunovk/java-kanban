import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = epics.get(taskId);
        if (task == null) {
            task = subtasks.get(taskId);
        }
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public void createEpic(String title, String description) {
        Epic epic = new Epic(title, description);
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    @Override
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

    @Override
    public void updateTaskStatus(int taskId, TaskStatus status) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(status);
            if (task instanceof Subtask) {
                updateEpicStatus(((Subtask) task).getEpicId());
            }
        }
    }

    @Override
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
        if (updatedTask instanceof Epic) {
            epics.put(updatedTask.getId(), (Epic) updatedTask);
        } else if (updatedTask instanceof Subtask) {
            subtasks.put(updatedTask.getId(), (Subtask) updatedTask);
        }
    }

    @Override
    public void deleteAllTasks() {
        epics.clear();
        subtasks.clear();
        idCounter = 1;
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void deleteTask(int taskId) {
        Task task = getTaskById(taskId);
        if (task instanceof Epic) {
            epics.remove(taskId);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            epics.get(subtask.getEpicId()).removeSubtask(subtask);
            subtasks.remove(taskId);
        }
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> epicSubtasks = epic.getSubtasks();
            for (Subtask subtask : epicSubtasks) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            subtasks.remove(subtaskId);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }
}