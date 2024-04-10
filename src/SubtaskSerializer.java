import java.util.List;
import java.util.stream.Collectors;

public class SubtaskSerializer {

    public static String serializeSubtasks(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(SubtaskSerializer::serializeSubtask)
                .collect(Collectors.joining("\n"));
    }

    private static String serializeSubtask(Subtask subtask) {
        return String.format("%s,%s,%d", subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
    }
}
