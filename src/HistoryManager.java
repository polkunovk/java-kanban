import java.util.List;

public interface HistoryManager {

    // Пустая строка добавлена!
    void add(Task task);

    // Пустая строка добавлена!
    void remove(int id);

    List<Task> getHistory();
}
