import java.util.ArrayList;
import java.util.List;

public class SubtaskDeserializer {

    public static List<Subtask> deserializeSubtasks(String data) {
        List<Subtask> subtasks = new ArrayList<>();
        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            String title = parts[0];
            String description = parts[1];
            int epicId = Integer.parseInt(parts[2]);
            Subtask subtask = new Subtask(title, description, epicId);
            subtasks.add(subtask);
        }
        return subtasks;
    }
}
