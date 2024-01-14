enum TaskStatus {
    НОВАЯ("NEW"),
    В_ПРОЦЕССЕ("IN_PROGRESS"),
    ЗАВЕРШЕНА("DONE");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
