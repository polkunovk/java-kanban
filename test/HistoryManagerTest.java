import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAdd() {
        Task task = new Task("Test Task", "Description");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertTrue(history.contains(task));
    }

    @Test
    void testRemove() {
        Task task = new Task("Test Task", "Description");
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void testDuplicate() {
        Task task = new Task("Test Task", "Description");
        historyManager.add(task);
        historyManager.add(task); // Adding duplicate
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size()); // Should contain only one instance of the task
    }
}
