import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    void createEpic(String title, String description);

    void createSubtask(String title, String description, int epicId);

    void updateTaskStatus(int taskId, TaskStatus status);

    List<Subtask> getAllSubtasksOfEpic(int epicId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubtask(int subtaskId);

    List<Task> getHistory();

    void addTask(Task task);

}