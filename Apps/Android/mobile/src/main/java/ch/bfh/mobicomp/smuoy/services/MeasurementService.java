package ch.bfh.mobicomp.smuoy.services;

import android.util.Log;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.entities.Sensor;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import ch.bfh.mobicomp.smuoy.utils.MagicMap;
import ch.bfh.mobicomp.smuoy.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles sensor measurements and groups them in a usable manner
 */
public class MeasurementService {
    private static final long DEFAULT_DELAY = 300;
    private static final String SERVICE_URL = "http://178.62.163.199/smoje/index.php/stations/";

    public static final MeasurementService measurementService = new MeasurementService();

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private HttpClient httpClient = new DefaultHttpClient();

    private Map<Smuoy, Map<String, List<Measurement>>> registry = new MagicMap<>(2, ArrayList.class);

    public void registerSensor(final Smuoy smuoy, final Sensor sensor) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = Utils.request(httpClient, new HttpGet(SERVICE_URL + smuoy.id + "/sensors/" + sensor.id + "/measurements"));

                    JSONArray jsonMeasurements = new JSONArray(response);

                    List<Measurement> measurements = new LinkedList<>();
                    for (int i = 0; i < jsonMeasurements.length(); i++) {
                        try {
                            JSONObject measurement = jsonMeasurements.getJSONObject(i);
                            measurements.add(new Measurement(smuoy.id, sensor, measurement));
                        } catch (JSONException e) {
                            Log.e("measurementService", e.getMessage(), e);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    private Measurement getMeasurement(List<Measurement> measurements, String unit) {
        for (Measurement measurement : measurements) {
            // FIXME: using the unit is just plain stupid and doesn't work as soon as we have water temperature.
            // There should be a useful name.
            if (unit.equalsIgnoreCase(measurement.getUnit())) {
                return measurement;
            }
        }
        return null;
    }

    public List<Measurement> getMeasurements(Smuoy smuoy, String type) {
        return registry.get(smuoy).get(type);
    }

    public LatLng getLocation(Smuoy smuoy) {
        return new LatLng(47.14227, 7.24343); // TODO
    }
}
