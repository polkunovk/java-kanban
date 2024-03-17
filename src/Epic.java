import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private static int idCounter = 1;
    private final List<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new ArrayList<>();
        setId(idCounter);
        idCounter++;
    }

    public void addSubtask(Subtask subtask) {
        if (subtask.getEpicId() != getId()) {
            throw new IllegalArgumentException("Нельзя добавить Подзадачу из другого Эпика.");
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

    public static String toString(Epic epic) {
        StringBuilder sb = new StringBuilder();
        sb.append(Task.toString(epic));

        for (Subtask subtask : epic.getSubtasks()) {
            sb.append(subtask.getId()).append(",");
        }
        return sb.toString();
    }

    public static Epic fromString(String value) {
        String[] parts = value.split(",");
        Epic epic = new Epic(parts[2], parts[4]);
        epic.setId(Integer.parseInt(parts[0]));
        epic.setStatus(TaskStatus.valueOf(parts[3]));

        for (int i = 5; i < parts.length; i++) {
            epic.addSubtask(new Subtask("", "", Integer.parseInt(parts[i])));
        }
        return epic;
    }
}
