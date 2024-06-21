package pl.ekoreo.worldeserver.utils;

import org.json.JSONObject;
import pl.ekoreo.worldeserver.enums.OutputTypes;

public class JsonUtils {
    public static JSONObject createJsonSingleMessage(String typeValue, String message){
        JSONObject json = new JSONObject();
        json.put("type", typeValue);
        json.put("data", message);
        return json;
    }
}
