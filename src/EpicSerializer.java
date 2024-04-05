import com.google.gson.Gson;
import java.util.List;

public class EpicSerializer {
    private static final Gson gson = new Gson();

    public static String serializeEpics(List<Epic> epics) {
        return gson.toJson(epics);
    }
}
