import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTests {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void taskEqualityById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи должны быть равны по ID");
    }

    @Test
    void epicEqualityById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики должны быть равны по ID");
    }

    @Test
    void cannotAddEpicToItself() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Epic epic = new Epic("Эпик", "Описание эпика");

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());

        assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(subtask));
    }

    @Test
    void cannotMakeSubtaskItsOwnEpic() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Epic epic = new Epic("Родительский Эпик", "Описание родительского эпика");
        Subtask subtask = new Subtask("Дочерняя Задача", "Описание дочерней задачи", epic.getId());

        assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(subtask));
    }


    @Test
    void addTaskToHistory() {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи");

        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу");
        assertTrue(historyManager.getHistory().contains(task), "История должна содержать добавленную задачу");
    }

    @Test
    void historyLimitedToMaxSize() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task("Задача " + i, "Описание задачи " + i);
            historyManager.add(task);
        }

        assertEquals(10, historyManager.getHistory().size(), "История должна быть ограничена по размеру");
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
}
