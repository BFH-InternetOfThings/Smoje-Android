package ch.bfh.mobicomp.smuoy.entities;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;

import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class ImageMeasurement extends Measurement {
    Uri uri;

    public ImageMeasurement(JSONArray measurements) {
        super(measurements);
        try {
            uri = Uri.parse(str(measurements.getJSONObject(0), "value", null));
        } catch (JSONException e) {
            Log.d("ImageMeasurement", "Can't get field 'value' from JSON", e);
        }
    }
}
