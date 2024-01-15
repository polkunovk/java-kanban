import java.util.ArrayList;
import java.util.List;

class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void updateStatus() {
        boolean allSubtasksDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);
        if (allSubtasksDone) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
