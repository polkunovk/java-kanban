import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private final String title;
    private final String description;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public static Task fromString(String value) {
        String[] parts = value.split(",");
        Task task = null;
        if (parts.length >= 5) {
            switch (parts[1]) {
                case "Epic":
                    task = new Epic(parts[2], parts[4]);
                    break;
                case "Subtask":
                    if (parts.length >= 7) {
                        task = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]));
                        ((Subtask) task).setStartTime(LocalDateTime.parse(parts[6]));
                    }
                    break;
                default:
                    break;
            }
            if (task != null) {
                task.setId(Integer.parseInt(parts[0]));
                task.setStatus(TaskStatus.valueOf(parts[3]));
                if (parts.length >= 6 && !parts[5].isEmpty()) {
                    task.setDuration(Duration.parse(parts[5]));
                }
                if (parts.length >= 7 && !parts[6].isEmpty()) {
                    ((Subtask) task).setStartTime(LocalDateTime.parse(parts[6]));
                }
            }
        }
        return task;
    }
}
