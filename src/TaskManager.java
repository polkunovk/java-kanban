import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();

    Task getTaskById(int taskId);

    void createEpic(String title, String description);

    void createSubtask(String title, String description, int epicId);

    void updateTaskStatus(int taskId, TaskStatus status);

    List<Subtask> getAllSubtasksOfEpic(int epicId);

    void deleteEpic(int epicId);

    void deleteSubtask(int subtaskId);

    List<Task> getHistory();

    void addTask(Task task);
}