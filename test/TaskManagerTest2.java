import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest2<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManagerInstance();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManagerInstance();
    }

    @Test
    void addAndGetAllTasksShouldIncreaseTaskListSizeAndContainAddedTask() {
        int initialSize = taskManager.getAllTasks().size();
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");
        taskManager.addTask(task);
        assertEquals(initialSize + 1, taskManager.getAllTasks().size());
        assertTrue(taskManager.getAllTasks().contains(task));
    }

    @Test
    void getTaskByIdShouldReturnTaskWithGivenId() {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void createEpicShouldCreateEpicTaskAndAddToTaskList() {
        taskManager.createEpic("Заголовок эпика", "Описание эпика");
        assertEquals(1, taskManager.getAllTasks().size());
        assertTrue(taskManager.getAllTasks().get(0) instanceof Epic);
    }

    @Test
    void createSubtaskShouldCreateSubtaskAndAddToTaskList() {
        taskManager.createEpic("Заголовок эпика", "Описание эпика");
        Epic epic = (Epic) taskManager.getAllTasks().get(0);
        taskManager.createSubtask("Заголовок подзадачи", "Описание подзадачи", epic.getId());
        assertEquals(2, taskManager.getAllTasks().size());
        assertTrue(taskManager.getAllTasks().get(1) instanceof Subtask);
    }

    @Test
    void updateTaskStatusShouldUpdateTaskStatus() {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");
        taskManager.addTask(task);
        taskManager.updateTaskStatus(task.getId(), TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void getAllSubtasksOfEpicShouldReturnSubtasksOfEpic() {
        taskManager.createEpic("Заголовок эпика", "Описание эпика");
        Epic epic = (Epic) taskManager.getAllTasks().get(0);
        taskManager.createSubtask("Заголовок подзадачи", "Описание подзадачи", epic.getId());
        List<Subtask> subtasks = taskManager.getAllSubtasksOfEpic(epic.getId());
        assertEquals(1, subtasks.size());
        assertEquals("Заголовок подзадачи", subtasks.get(0).getTitle());
    }

    @Test
    void deleteEpicShouldDeleteEpicAndRemoveFromTaskList() {
        taskManager.createEpic("Заголовок эпика", "Описание эпика");
        Epic epic = (Epic) taskManager.getAllTasks().get(0);
        taskManager.deleteEpic(epic.getId());
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteSubtaskShouldDeleteSubtaskAndKeepEpicInTaskList() {
        taskManager.createEpic("Заголовок эпика", "Описание эпика");
        Epic epic = (Epic) taskManager.getAllTasks().get(0);
        taskManager.createSubtask("Заголовок подзадачи", "Описание подзадачи", epic.getId());
        Subtask subtask = (Subtask) taskManager.getAllTasks().get(1);
        taskManager.deleteSubtask(subtask.getId());
        assertEquals(1, taskManager.getAllTasks().size()); // Эпик должен остаться
    }

    @Test
    void getHistoryShouldReturnListOfTasksAdded() {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");
        taskManager.addTask(task);
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals("Тестовая задача", history.get(0).getTitle());
    }

    @Test
    void getPrioritizedTasksShouldReturnTasksSortedByPriority() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.updateTaskStatus(task1.getId(), TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatus(task2.getId(), TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatus(task3.getId(), TaskStatus.DONE);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size());
        assertEquals("Задача 1", prioritizedTasks.get(0).getTitle()); // Задача 1 - наивысший приоритет
        assertEquals("Задача 2", prioritizedTasks.get(1).getTitle()); // Задача 2 - высший приоритет
        assertEquals("Задача 3", prioritizedTasks.get(2).getTitle()); // Задача 3 - обычный приоритет
    }

    abstract void isTaskTimeOverlapShouldCheckIfTasksOverlap();
}
