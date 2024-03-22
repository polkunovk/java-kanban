import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;
    private final Map<Integer, Integer> taskIdToIndex;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
        this.taskIdToIndex = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (taskIdToIndex.containsKey(taskId)) {
            int indexToRemove = taskIdToIndex.get(taskId);
            history.remove(indexToRemove);
        }
        history.add(task);
        taskIdToIndex.put(taskId, history.size() - 1);
    }

    @Override
    public void remove(int id) {
        if (taskIdToIndex.containsKey(id)) {
            int indexToRemove = taskIdToIndex.get(id);
            history.remove(indexToRemove);

            for (int i = indexToRemove; i < history.size(); i++) {
                taskIdToIndex.put(history.get(i).getId(), i);
            }
            taskIdToIndex.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
