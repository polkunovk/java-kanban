import java.util.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic epic = new Epic("Важный эпик", "Описание важного эпика");
        Subtask subtask1 = new Subtask("Задача 1", "Описание задачи 1", epic.getId());
        Subtask subtask2 = new Subtask("Задача 2", "Описание задачи 2", epic.getId());

        taskManager.createTask(epic);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);

        subtask1.setStatus(TaskStatus.В_ПРОЦЕССЕ);
        taskManager.updateTask(subtask1);

        epic.updateStatus();
        taskManager.updateTask(epic);

        List<Task> allTasks = taskManager.getAllTasks();
        for (Task task : allTasks) {
            System.out.println("Task ID: " + task.getId() + ", Title: " + task.getTitle() +
                    ", Status: " + task.getStatus());
        }

        List<Subtask> epicSubtasks = taskManager.getAllSubtasksOfEpic(epic.getId());
        for (Subtask subtask : epicSubtasks) {
            System.out.println("Subtask ID: " + subtask.getId() + ", Title: " + subtask.getTitle() +
                    ", Epic ID: " + subtask.getEpicId() + ", Status: " + subtask.getStatus());
        }
    }
}
