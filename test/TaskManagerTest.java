import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    void taskEqualityById() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи должны быть равны по ID");
    }

    @Test
    void epicEqualityById() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики должны быть равны по ID");
    }

    @Test
    void addTaskToHistory() {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");

        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу");
        assertTrue(historyManager.getHistory().contains(task), "История должна содержать добавленную задачу");
    }

    @Test
    void checkNewTaskStatusToString() {
        assertEquals("НОВАЯ", TaskStatus.NEW.toString(), "Строковое представление NEW должно быть 'НОВАЯ'");
    }

    @Test
    void checkInProgressTaskStatusToString() {
        assertEquals("В_ПРОЦЕССЕ", TaskStatus.IN_PROGRESS.toString(), "Строковое представление IN_PROGRESS должно быть 'В_ПРОЦЕССЕ'");
    }

    @Test
    void checkDoneTaskStatusToString() {
        assertEquals("ЗАВЕРШЕНА", TaskStatus.DONE.toString(), "Строковое представление DONE должно быть 'ЗАВЕРШЕНА'");
    }

    @Test
    void verifyEnumValues() {
        TaskStatus[] values = TaskStatus.values();

        assertEquals(3, values.length, "Количество значений перечисления должно быть равно 3");

        assertEquals(TaskStatus.NEW, values[0], "Первый элемент перечисления должен быть NEW");
        assertEquals(TaskStatus.IN_PROGRESS, values[1], "Второй элемент перечисления должен быть IN_PROGRESS");
        assertEquals(TaskStatus.DONE, values[2], "Третий элемент перечисления должен быть DONE");
    }

    @Test
    void removeTaskFromHistory() {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу");

        historyManager.remove(task.getId());

        assertEquals(0, historyManager.getHistory().size(), "История должна быть пустой после удаления задачи");
    }

    @Test
    void deletedSubtasksShouldNotContainOldIds() {
        Epic epic = new Epic("Родительский Эпик", "Описание родительского эпика");
        Subtask subtask = new Subtask("Дочерняя Задача", "Описание дочерней задачи", epic.getId());
        historyManager.add(subtask);

        taskManager.deleteSubtask(subtask.getId());

        assertFalse(epic.getSubtasks().contains(subtask), "Эпик не должен содержать удаленную подзадачу");
    }

    @Test
    void deletedEpicsShouldNotContainOldSubtaskIds() {
        Epic epic = new Epic("Родительский Эпик", "Описание родительского эпика");
        Subtask subtask = new Subtask("Дочерняя Задача", "Описание дочерней задачи", epic.getId());
        epic.addSubtask(subtask);
        historyManager.add(epic);

        taskManager.deleteEpic(epic.getId());

        assertFalse(historyManager.getHistory().contains(subtask), "История не должна содержать id удаленной подзадачи из удаленного эпика");
    }
}
