import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTests {

    @Test
    void taskEqualityById() {
        new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи должны быть равны по ID");
    }

    @Test
    void epicEqualityById() {
        new InMemoryTaskManager(new InMemoryHistoryManager());
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики должны быть равны по ID");
    }

    @Test
    void cannotAddEpicToItself() {
        new InMemoryTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic("Эпик", "Описание эпика");

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());

        assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(subtask));
    }

    @Test
    void cannotMakeSubtaskItsOwnEpic() {
        new InMemoryTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic("Родительский Эпик", "Описание родительского эпика");
        Subtask subtask = new Subtask("Дочерняя Задача", "Описание дочерней задачи", epic.getId());

        assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(subtask));
    }
}
