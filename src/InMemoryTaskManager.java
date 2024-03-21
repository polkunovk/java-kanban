import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.add(epic);
            List<Subtask> epicSubtasks = epic.getSubtasks();
            for (Subtask subtask : epicSubtasks) {
                historyManager.add(subtask);
            }
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void createEpic(String title, String description) {
        Epic epic = new Epic(title, description);
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
        historyManager.add(epic);
        System.out.println("Создан Эпик с ID: " + epic.getId());
    }

    @Override
    public void createSubtask(String title, String description, int epicId) {
        Epic epic = epics.get(epicId);
        System.out.println("Попытка создать Подзадачу для Эпика с ID: " + epicId);
        if (epic != null) {
            Subtask subtask = new Subtask(title, description, epicId);
            subtask.setId(idCounter++);
            subtasks.put(subtask.getId(), subtask);

            if (subtask.getEpicId() != epic.getId()) {
                throw new IllegalArgumentException("Нельзя добавить Подзадачу из другого Эпика.");
            }

            epic.addSubtask(subtask);
            updateEpicStatus(subtask.getEpicId());
            historyManager.add(subtask);
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
            historyManager.add(task);
        }
    }

    @Override
    public List<Subtask> getAllSubtasksOfEpic(int epicId) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epicSubtasks.addAll(epic.getSubtasks());
            for (Subtask subtask : epicSubtasks) {
                historyManager.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public void deleteAllTasks() {
        epics.clear();
        subtasks.clear();
        idCounter = 1;
        historyManager.getHistory().clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            List<Subtask> epicSubtasks = epic.getSubtasks();
            for (Subtask subtask : epicSubtasks) {
                subtasks.remove(subtask.getId());
            }
        }
        epics.clear();
        historyManager.getHistory().removeIf(task -> task instanceof Epic);
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            int epicId = subtask.getEpicId();
            epics.get(epicId).removeSubtask(subtask);
        }
        subtasks.clear();
        historyManager.getHistory().removeIf(task -> task instanceof Subtask);
    }

    @Override
    public void deleteTask(int taskId) {
        Task task = getTaskById(taskId);
        if (task != null) {
            if (task instanceof Epic) {
                epics.remove(taskId);
            } else if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                epics.get(subtask.getEpicId()).removeSubtask(subtask);
                subtasks.remove(taskId);
            }
            historyManager.remove(taskId);
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
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            epics.get(epicId).removeSubtask(subtask);
            subtasks.remove(subtaskId);
            updateEpicStatus(epicId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            boolean allSubtasksDone = true;
            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStatus() != TaskStatus.DONE) {
                    allSubtasksDone = false;
                    break;
                }
            }
            if (allSubtasksDone) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
            historyManager.add(epic);
        }
    }

    @Override
    public void addTask(Task task) {
        if (task instanceof Epic) {
            createEpic(task.getTitle(), task.getDescription());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            createSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
        }
    }
}