package pl.ekoreo.worldeserver.utils;

import org.json.JSONException;
import org.json.JSONObject;
import pl.ekoreo.worldeserver.enums.OutputTypes;

public class JsonUtils {
    public static String createErrorJsonMessage(String message){
        JSONObject json = new JSONObject();
        json.put("type", OutputTypes.ERROR.value);
        json.put("data", message);
        return json.toString();
    }
    public static JSONObject createJsonSingleMessage(String typeValue, String message){
        JSONObject json = new JSONObject();
        json.put("type", typeValue);
        json.put("data", message);
        return json;
    }
    public static JSONObject createJsonSingleMessage(String typeValue, JSONObject data){
        JSONObject json = new JSONObject();
        json.put("type", typeValue);
        json.put("data", data);
        return json;
    }
}
