package ch.bfh.mobicomp.smuoy.entities;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

public class Smuoy implements Serializable {
    public final String id;
    public final String name;
    public final String description;
    private final Map<String, Sensor> sensors = new HashMap<>();

    public SensorListener listener;

    public Smuoy(JSONObject json) {
        id = str(json, "stationId", "");
        name = str(json, "name", "");
        description = str(json, "description", "");
    }

    public synchronized void addSensor(Sensor sensor){
        sensors.put(sensor.id, sensor);
    }

    public synchronized void updateSensors(List<Sensor> newSensors) {
        Set<String> oldIds = new HashSet<>(sensors.keySet());

        for (Sensor s : newSensors) {
            if (listener != null) {
                if (!sensors.containsKey(s.id)) {
                    listener.added(s);
                } else {
                    oldIds.remove(s.id);
                }
            }
            sensors.put(s.id, s);
        }
        for (String id : oldIds) {
            Sensor removed = sensors.remove(id);
            if (listener != null) {
                listener.removed(removed);
            }
        }
    }

    public synchronized void setListener(SensorListener listener) {
        this.listener = listener;
        for (Sensor s : sensors.values()) {
            listener.added(s);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static interface SensorListener {
        void added(Sensor sensor);

        void removed(Sensor sensor);
    }
}
