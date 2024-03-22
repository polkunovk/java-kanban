public enum  TaskStatus {
    NEW("НОВАЯ"),
    IN_PROGRESS("В_ПРОЦЕССЕ"),
    DONE("ЗАВЕРШЕНА");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}