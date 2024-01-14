import java.util.ArrayList;
import java.util.List;

class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new ArrayList<>();
    }

    public void updateStatus() {
        boolean allSubtasksDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.ЗАВЕРШЕНА);
        if (allSubtasksDone) {
            setStatus(TaskStatus.ЗАВЕРШЕНА);
        } else {
            setStatus(TaskStatus.В_ПРОЦЕССЕ);
        }
    }
}
