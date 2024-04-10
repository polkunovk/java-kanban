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
    public void saveAndLoadEmptyTaskManager() {
        File saveFile = createTempFile();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), saveFile);
        taskManager.saveToFile();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(saveFile, new InMemoryHistoryManager());

        assertTrue(loadedTaskManager.getAllTasks().isEmpty());
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