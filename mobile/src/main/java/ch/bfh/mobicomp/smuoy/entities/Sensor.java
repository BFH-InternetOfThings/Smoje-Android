package ch.bfh.mobicomp.smuoy.entities;

import ch.bfh.mobicomp.smuoy.cards.CardUpdater;
import org.json.JSONObject;

import java.io.Serializable;

import static ch.bfh.mobicomp.smuoy.utils.Utils.num;
import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Sensor implements Serializable {
    public final String id;
    public final String name;
    public final int delay; // in seconds
    public final String status;
    public final String unit;

    public final int displayType;

    private Measurement lastMeasurement;
    private CardUpdater updater;

    public Sensor(JSONObject json) {
        id = str(json, "sensorId", "");
        name = str(json, "name", "");
        delay = num(json, "delay", 60);
        status = str(json, "status", "");
        unit = str(json, "unit", "");
        displayType = num(json, "displayTypeId", 3);
    }

    public void update(Measurement newMeasurement) {
        lastMeasurement = newMeasurement;
        if (updater != null) {
            updater.update(newMeasurement);
        }
    }

    public void setUpdater(CardUpdater updater) {
        this.updater = updater;
        if (updater != null && lastMeasurement != null) {
            updater.update(lastMeasurement);
        }
    }
}
