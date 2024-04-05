import com.google.gson.Gson;

public class EpicDeserializer {
    private static final Gson gson = new Gson();

    public static Epic deserializeEpic(String json) {
        return gson.fromJson(json, Epic.class);
    }
}
