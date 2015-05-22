package ch.bfh.mobicomp.smuoy.entities;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private List<WeakReference<LocationUpdater>> updaters = new LinkedList<>();

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

    public void addUpdater(LocationUpdater updater) {
        updaters.add(new WeakReference<>(updater));
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
        for (WeakReference<LocationUpdater> updater : updaters) {
            if (updater.get() != null) {
                updater.get().update(location);
            }
        }
    }

    public interface SensorListener {
        void added(Sensor sensor);
    }

    public interface LocationUpdater {
        void update(LatLng location);
    }
}
