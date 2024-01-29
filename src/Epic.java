import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        if (subtask.getEpicId() == getId()) {
            throw new IllegalArgumentException("Cannot add the Epic itself as a Subtask.");
        }
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void updateStatus() {
        boolean allSubtasksDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);
        if (allSubtasksDone) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
