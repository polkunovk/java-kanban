import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    @Test
    public void testEpicStatusAllSubtasksNew() {
        Epic epic = new Epic("Тестовый Эпик", "Описание");
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }
}
