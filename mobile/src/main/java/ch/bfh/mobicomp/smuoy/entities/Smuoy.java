package ch.bfh.mobicomp.smuoy.entities;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

public class Smuoy implements Serializable {
    public final String id;
    public final String name;
    public final String description;
    public final String urlGPS;
    private final Map<String, Sensor> sensors = new HashMap<>();

    private LatLng location;

    public SensorListener listener;
    private LocationUpdater updater;

    public Smuoy(JSONObject json) {
        id = str(json, "stationId", "");
        name = str(json, "name", "");
        description = str(json, "description", "");
        urlGPS = str(json, "urlTissan", "");
    }

    public synchronized void addSensor(Sensor sensor) {
        sensors.put(sensor.id, sensor);
    }

    public synchronized void setListener(SensorListener listener) {
        this.listener = listener;
        for (Sensor s : sensors.values()) {
            listener.added(s);
        }
    }

    public void setUpdater(LocationUpdater updater) {
        this.updater = updater;
        if (location != null) {
            updater.update(location);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public void updateLocation(LatLng location) {
        this.location = location;
        if (updater != null) {
            updater.update(location);
        }
    }

    public static interface SensorListener {
        void added(Sensor sensor);
    }

    public static interface LocationUpdater {
        public void update(LatLng location);
    }
}
