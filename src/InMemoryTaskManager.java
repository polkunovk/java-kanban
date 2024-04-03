import java.time.LocalDateTime;
import java.util.*;

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
            updateEpicStatus(epic.getId());
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

    @Override
    public void addTask(Task task) {
        if (task instanceof Epic) {
            createEpic(task.getTitle(), task.getDescription());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            createSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> allTasks = new ArrayList<>(getAllTasks());
        allTasks.sort(Comparator.comparing(task -> task.getStartTime() != null ? task.getStartTime() : LocalDateTime.MAX));
        return allTasks;
    }

    @Override
    public boolean isTaskTimeOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getDuration() == null ||
                task2.getStartTime() == null || task2.getDuration() == null)
            return false;

        LocalDateTime task1Start = task1.getStartTime();
        LocalDateTime task1End = task1Start.plus(task1.getDuration());
        LocalDateTime task2Start = task2.getStartTime();
        LocalDateTime task2End = task2Start.plus(task2.getDuration());

        return (task1Start.isBefore(task2End) && task2Start.isBefore(task1End));
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            boolean allSubtasksDone = true;
            LocalDateTime earliestStartTime = null;
            LocalDateTime latestEndTime = null;
            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStatus() != TaskStatus.DONE) {
                    allSubtasksDone = false;
                    break;
                }
                if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                    earliestStartTime = subtask.getStartTime();
                }
                LocalDateTime subtaskEndTime = subtask.getStartTime().plus(subtask.getDuration());
                if (latestEndTime == null || subtaskEndTime.isAfter(latestEndTime)) {
                    latestEndTime = subtaskEndTime;
                }
            }
            if (allSubtasksDone) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
            epic.setStartTime(earliestStartTime);
            epic.setEndTime(latestEndTime);
            historyManager.add(epic);
        }
    }
}
