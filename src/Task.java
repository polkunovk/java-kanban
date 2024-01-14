class Task implements Identifiable {
    private static int idCounter = 1;

    private final int id;
    private final String title;
    private final String description;
    private TaskStatus status;

    public Task(String title, String description) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.status = TaskStatus.НОВАЯ;
    }

    public int getId() {
        return id;
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
}
