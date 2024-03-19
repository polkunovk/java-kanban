import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    public void saveAndLoadEmptyFile() {
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
