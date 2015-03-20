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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ch.bfh.mobicomp.smuoy.utils.Utils.dec;

/**
 * Handles sensor measurements and groups them in a usable manner
 */
public class MeasurementService {
    private static final long DEFAULT_DELAY = 30000;
    private static final long MIN_DELAY = 5000;
    private static final String SERVICE_URL = "http://smoje.ch/smoje/index.php/stations/";

    public static final MeasurementService measurementService = new MeasurementService();

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private HttpClient httpClient = new DefaultHttpClient();

    private Map<Smuoy, Map<String, List<Measurement>>> registry = new MagicMap<>(2, ArrayList.class);

    public void registerSmuoy(final Smuoy smuoy) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = Utils.request(httpClient, new HttpGet(smuoy.urlGPS));

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject lastPosition = jsonResponse.getJSONObject("lastPosition");
                    double latitude = dec(lastPosition, "latitude", 0);
                    double longitude = dec(lastPosition, "longitude", 0);
                    LatLng latLng = new LatLng(latitude, longitude);
                    smuoy.updateLocation(latLng);
                } catch (Exception e) {
                    Log.e("GPS", e.getMessage(), e);
                }
                scheduler.schedule(this, 5, TimeUnit.MINUTES);
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    public void registerSensor(final Smuoy smuoy, final Sensor sensor) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                long nextExecutionTime = 0;
                try {
                    String response = Utils.request(httpClient, new HttpGet(SERVICE_URL + smuoy.id + "/sensors/measurements"));

                    JSONArray jsonMeasurements = getMeasurements(sensor, response);

                    if (jsonMeasurements != null) {
                        for (int i = 0; i < jsonMeasurements.length(); i++) {
                            try {
                                JSONObject measurement = jsonMeasurements.getJSONObject(i);
                                Measurement m = new Measurement(smuoy.id, sensor, measurement); // this is ugly - side effect
                                nextExecutionTime = m.getNextExecutionTime() > nextExecutionTime ? m.getNextExecutionTime() : nextExecutionTime;
                            } catch (JSONException e) {
                                Log.e("measurementService", e.getMessage(), e);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("measurementService", e.getMessage(), e);
                }
                long delay = sensor.delay;
                if (nextExecutionTime > 0) {
                    delay = nextExecutionTime - System.currentTimeMillis() + 500;
                    if (delay < MIN_DELAY) {
                        if (delay < 0) {
                            delay = DEFAULT_DELAY;
                        } else {
                            delay = MIN_DELAY;
                        }
                    }
                }
                scheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    private JSONArray getMeasurements(Sensor sensor, String response) throws JSONException {
        JSONObject responseObject = new JSONObject(response);
        JSONArray stations = responseObject.getJSONArray("station");
        JSONObject station = stations.getJSONObject(0);
        JSONArray sensors = station.getJSONArray("sensors");
        for (int i = 0; i < sensors.length(); i++) {
            JSONObject sensorObject = sensors.getJSONObject(i);
            if (sensorObject.getString("sensorId").equals(sensor.id) && sensorObject.has("measurements")) {
                return sensorObject.getJSONArray("measurements");
            }
        }
        return null;
    }
}
