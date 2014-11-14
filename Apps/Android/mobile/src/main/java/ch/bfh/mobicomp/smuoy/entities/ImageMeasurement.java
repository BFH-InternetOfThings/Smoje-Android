package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;

import java.net.URL;

import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class ImageMeasurement extends Measurement {
    public final URL url;

    public ImageMeasurement(JSONArray measurements) {
        super(measurements);
        URL tempURL = null;
        try {
            tempURL = new URL(str(measurements.getJSONObject(0), "value", null));
        } catch (Exception e) {
            Log.d("ImageMeasurement", "Can't get field 'value' from JSON", e);
        }
        url = tempURL;
    }
}
