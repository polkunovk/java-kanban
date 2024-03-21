import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FileBackedTaskManagerTest {

    @Test
    public void saveAndLoadEmptyFile() throws IOException {
        File saveFile = createTempFile();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), saveFile);
        taskManager.saveToFile();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(saveFile, new InMemoryHistoryManager());

        assertTrue(loadedTaskManager.getAllTasks().isEmpty());
    }

    @Test
    public void saveAndLoadMultipleTasks() throws IOException {
        File saveFile = createTempFile();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), saveFile);
        taskManager.createEpic("Эпик 1", "Описание Эпика 1");
        taskManager.createSubtask("Подзадача 1", "Описание Подзадачи 1", 1);
        taskManager.createSubtask("Подзадача 2", "Описание Подзадачи 2", 1);
        taskManager.saveToFile();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(saveFile, new InMemoryHistoryManager());

        int epicsCount = (int) loadedTaskManager.getAllTasks().stream().filter(task -> task instanceof Epic).count();
        int subtasksCount = (int) loadedTaskManager.getAllTasks().stream().filter(task -> task instanceof Subtask).count();

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
