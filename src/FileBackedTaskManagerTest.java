import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FileBackedTaskManagerTest {

    @Test
    public void saveAndLoadEmptyFile() {
        File saveFile = createTempFile();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), saveFile);
        taskManager.saveToFile();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(saveFile, new InMemoryHistoryManager());

        assertTrue(loadedTaskManager.getAllTasks().isEmpty());
    }

    @Test
    public void saveAndLoadMultipleTasks() {
        File saveFile = createTempFile();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), saveFile);
        taskManager.createEpic("Эпик 1", "Описание Эпика 1");
        taskManager.createSubtask("Подзадача 1", "Описание Подзадачи 1", 1);
        taskManager.createSubtask("Подзадача 2", "Описание Подзадачи 2", 1);
        taskManager.saveToFile();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(saveFile, new InMemoryHistoryManager());

        int epicsCount = loadedTaskManager.getAllTasks().stream().filter(task -> task instanceof Epic).mapToInt(task -> 1).sum();
        int subtasksCount = loadedTaskManager.getAllTasks().stream().filter(task -> task instanceof Subtask).mapToInt(task -> 1).sum();

        assertEquals(1, epicsCount);
        assertEquals(2, subtasksCount);
    }

    private File createTempFile() {
        try {
            return File.createTempFile("temp", ".txt");
        } catch (IOException e) {
            fail("Ошибка создания временного файла");
            return null;
        }
    }
}
