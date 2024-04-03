import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Subtask> subtasks;
    private Duration totalDuration;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new ArrayList<>();
        this.totalDuration = Duration.ZERO;
        this.endTime = null;
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void addSubtask(Subtask subtask) {
        if (subtask.getEpicId() != getId()) {
            throw new IllegalArgumentException("Нельзя добавить Подзадачу из другого Эпика.");
        }
        subtasks.add(subtask);
        totalDuration = totalDuration.plus(subtask.getDuration());
        updateEpicTime();
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.remove(subtask)) {
            totalDuration = totalDuration.minus(subtask.getDuration());
            updateEpicTime();
        }
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private void updateEpicTime() {
        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;

        for (Subtask subtask : subtasks) {
            LocalDateTime subtaskStartTime = subtask.getStartTime();
            LocalDateTime subtaskEndTime = subtaskStartTime.plus(subtask.getDuration());

            if (earliestStartTime == null || subtaskStartTime.isBefore(earliestStartTime)) {
                earliestStartTime = subtaskStartTime;
            }

            if (latestEndTime == null || subtaskEndTime.isAfter(latestEndTime)) {
                latestEndTime = subtaskEndTime;
            }
        }

        setStartTime(earliestStartTime);
        endTime = latestEndTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + totalDuration +
                ", startTime=" + getStartTime() +
                ", endTime=" + endTime +
                '}';
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
