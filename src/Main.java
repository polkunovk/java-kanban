import java.util.List;

class Main {
    public static void main(String[] args) {

        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

        taskManager.createEpic("Важный эпик", "Описание важного эпика");
        taskManager.createSubtask("Задача 1", "Описание задачи 1", 1);
        taskManager.createSubtask("Задача 2", "Описание задачи 2", 1);

        taskManager.updateTaskStatus(2, TaskStatus.IN_PROGRESS);

        List<Task> allTasks = taskManager.getAllTasks();
        for (Task task : allTasks) {
            System.out.println("Task ID: " + task.getId() + ", Title: " + task.getTitle() +
                    ", Status: " + task.getStatus());
        }

        List<Subtask> epicSubtasks = taskManager.getAllSubtasksOfEpic(1);
        for (Subtask subtask : epicSubtasks) {
            System.out.println("Subtask ID: " + subtask.getId() + ", Title: " + subtask.getTitle() +
                    ", Epic ID: " + subtask.getEpicId() + ", Status: " + subtask.getStatus());
        }


        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
