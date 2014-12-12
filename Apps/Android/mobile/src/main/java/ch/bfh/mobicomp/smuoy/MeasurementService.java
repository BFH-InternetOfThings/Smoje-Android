package ch.bfh.mobicomp.smuoy;

import android.util.Log;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.entities.Sensor;
import ch.bfh.mobicomp.smuoy.entities.SensorType;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import ch.bfh.mobicomp.smuoy.utils.MagicMap;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Handles sensor measurements and groups them in a usable manner
 */
public class MeasurementService {
    private static final long MIN_TIME_BETWEEN_REQUESTS = 10000;
    private static final String SERVICE_URL = "http://178.62.163.199/smoje/index.php/Measurements";
    public static final MeasurementService measurementService = new MeasurementService();

    private Map<Smuoy, Map<SensorType, List<Measurement>>> registry = new MagicMap<>(2, ArrayList.class);

    public void registerSensor(Smuoy smuoy, Sensor sensor, JSONArray jsonMeasurements) {
        List<SensorType> types = SensorType.from(sensor);
        List<Measurement> measurements = getMeasurements(jsonMeasurements);

        for (SensorType type : types) {
            Measurement m = null;
            switch (type) {
                case TEMPERATURE_AIR:
                    m = getMeasurement(measurements, "celsius");
                    break;
                case ATMOSPHERIC_PRESSURE:
                    m = getMeasurement(measurements, "bar");
                    break;
                case HUMIDITY:
                    m = getMeasurement(measurements, "null"); // FIXME: What The FUCK?
                    break;
                // FIXME: there could be more...
                default:
                    for (Measurement measurement : measurements) {
                        measurement.setType(type);
                        registry.get(smuoy).get(type).add(measurement);
                    }
                    break;
            }
            if (m != null) {
                m.setType(type);
                registry.get(smuoy).get(type).add(m);
            }
        }
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

    public List<Measurement> getMeasurements(Smuoy smuoy, SensorType type) {
        return registry.get(smuoy).get(type);
    }

    public LatLng getLocation(Smuoy smuoy) {
        return null;
    }

    private List<Measurement> getMeasurements(JSONArray measurements) {
        List<Measurement> result = new LinkedList<>();
        for (int i = 0; i < measurements.length(); i++) {
            try {
                JSONObject measurement = measurements.getJSONObject(i);
                result.add(new Measurement(measurement));
            } catch (JSONException e) {
                Log.e("measurementService", e.getMessage(), e);
            }
        }
        return result;
    }
}
