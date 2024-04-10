
import java.io.*;

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

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    private void loadFromFile() {
        getAllTasks().clear();
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
                return;
            }
            String fileContent = readFile(saveFile);
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                Task task = Task.fromString(line.trim());
                if (task != null) {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (Task task : getAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл", e);
        }
    }

    private String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {
        return new FileBackedTaskManager(historyManager, file);
    }
}
