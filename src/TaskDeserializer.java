import com.google.gson.Gson;

public class TaskDeserializer {
    private static final Gson gson = new Gson();

    public static Task deserializeTask(String json) {
        return gson.fromJson(json, Task.class);
    }
}
