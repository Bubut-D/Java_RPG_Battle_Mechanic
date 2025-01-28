package GameResources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.util.ArrayList;

public class JsonCharacterReader {
    private static Gson gson;

    static {gson = new Gson();}

    public static ArrayList<GameCharacter> getFromJson(String fileName){
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, new TypeToken<ArrayList<GameCharacter>>() {}.getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
