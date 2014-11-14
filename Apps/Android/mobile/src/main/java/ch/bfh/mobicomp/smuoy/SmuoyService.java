package ch.bfh.mobicomp.smuoy;

import android.util.Log;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 14.11.14.
 */
public class SmuoyService {
    public static final SmuoyService smuoyService = new SmuoyService();

    private SmuoyService() {
    }

    public List<Smuoy> getSmuoys() {
        List<Smuoy> result = new LinkedList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(new Smuoy(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e("SmuoyService", "getSmuoys", e);
        }
        return result;
    }

    public Smuoy getSmuoy(String id) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject smuoyObject = jsonArray.getJSONObject(i);
                if (id.equals(str(smuoyObject, "id", ""))) {
                    return new Smuoy(smuoyObject);
                }
            }
        } catch (JSONException e) {
            Log.e("SmuoyService", "getSmuoys", e);
        }
        return null;
    }

    private String json = "[\n" +
            "\t{\n" +
            "\t\t\"id\" : \"1\",\n" +
            "\t\t\"name\" : \"Ἄρτεμις 🎱\",\n" +
            "\t\t\"description\" : \"\",\n" +
            "\t\t\"sensors\" : [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\"name\" : \"gps\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 60, // seconds\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\t\"name\" : \"location\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\t\t\"name\" : \"lat\",\n" +
            "\t\t\t\t\t\t\"value\" : 47.102382,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°N\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.007+0100\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\t\t\"name\" : \"lon\",\n" +
            "\t\t\t\t\t\t\"value\" : 7.196388,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°E\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.007+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"2\",\n" +
            "\t\t\t\t\"name\" : \"wind\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 120,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"2\",\n" +
            "\t\t\t\t\t\"name\" : \"wind\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"4\",\n" +
            "\t\t\t\t\t\t\"name\" : \"speed\",\n" +
            "\t\t\t\t\t\t\"value\" : 5.1,\n" +
            "\t\t\t\t\t\t\"unit\" : \"km/h\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.128+0100\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"4\",\n" +
            "\t\t\t\t\t\t\"name\" : \"direction\",\n" +
            "\t\t\t\t\t\t\"value\" : 136.916481,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.128+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\"name\" : \"air temperature\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 90,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\"name\" : \"temperature\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\t\"name\" : \"air temperature\",\n" +
            "\t\t\t\t\t\t\"value\" : 10.6128641024,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°C\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:15:12.064+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\"name\" : \"water temperature\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 90,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\"name\" : \"temperature\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\t\"name\" : \"water temperature\",\n" +
            "\t\t\t\t\t\t\"value\" : 12.012800641024,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°C\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:15:12.640+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"4\",\n" +
            "\t\t\t\t\"name\" : \"selfie\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 3600,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\"name\" : \"image\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"5\",\n" +
            "\t\t\t\t\t\t\"name\" : \"image\",\n" +
            "\t\t\t\t\t\t\"value\" : \"http://www.fischerforum.ch/coppermine/albums/userpics/10002/070802_Bielersee_Ferien_04.jpg\",\n" +
            "\t\t\t\t\t\t\"unit\" : \"image/jpeg\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T13:21:12.640+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t}\n" +
            "\t\t\t// more...?\n" +
            "\t\t]\n" +
            "\t}\n" +
            "\t// more...?\n" +
            "]";
}
