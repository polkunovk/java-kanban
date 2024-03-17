import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File saveFile;

    public FileBackedTaskManager(HistoryManager historyManager, File saveFile) {
        super(historyManager);
        this.saveFile = saveFile;
        loadFromFile();
    }

    public void saveToFile() {
        save();
    }

    public void addTask(Task task) {
        if (task instanceof Epic) {
            super.createEpic(task.getTitle(), task.getDescription());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            super.createSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
        }
        save();
    }

    private void loadFromFile() {
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            String fileContent = readFile(saveFile);
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                Task task = Task.fromString(line.trim());
                if (task != null) {
                    if (task instanceof Epic) {
                        addTask(task);
                    } else if (task instanceof Subtask) {
                        addTask(task);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла : " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (Task task : getAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(File file) throws IOException {
        return Files.readString(file.toPath());
    }

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {
        return new FileBackedTaskManager(historyManager, file);
    }
}
