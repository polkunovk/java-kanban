import com.google.gson.Gson;

import java.io.IOException;

public class Practicum {


    // IOException могут сгенерировать методы create() и bind(...)
    public static void main(String[] args) throws IOException {
        LikesInfo likesInfo = new LikesInfo();
        likesInfo.setRepostsCount(10);
        likesInfo.setHasOwnerLiked(true);
        likesInfo.setLikes(new Like[]{
                new Like("Алексей", "http://example.com/avatars/aleksey.jpg"),
                new Like("Елена", "http://example.com/avatars/elena.jpg"),
                new Like("Света", "http://example.com/avatars/sveta.jpg"),
        });

        // код сериализации
    }
}