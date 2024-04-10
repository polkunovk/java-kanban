import com.google.gson.Gson;
import java.util.List;

public class TaskSerializer {
    private static final Gson gson = new Gson();

    public static String serializeTasks(List<Task> tasks) {
        return gson.toJson(tasks);
    }
}
