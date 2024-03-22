import java.util.Objects;

public class Task {
    private int id;
    private final String title;
    private final String description;
    private TaskStatus status;

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

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getClass().getSimpleName()).append(",");
        sb.append(task.getTitle()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        return sb.toString();
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
                    if (parts.length >= 6) {
                        task = new Subtask(parts[2], parts[4], Integer.parseInt(parts[5]));
                    }
                    break;
                default:
                    break;
            }
            if (task != null) {
                task.setId(Integer.parseInt(parts[0]));
                task.setStatus(TaskStatus.valueOf(parts[3]));
            }
        }
        return task;
    }

    public String getDescription() {
        return description;
    }
}
