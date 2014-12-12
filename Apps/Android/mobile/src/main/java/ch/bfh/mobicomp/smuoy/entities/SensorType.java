package ch.bfh.mobicomp.smuoy.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * FIXME: this class (especially the sensorTypes) must be cleaned up once the sensor types from JSON start to make sense.
 */
public enum SensorType {
    POSITION("gps"),

    TEMPERATURE_AIR("temperature/pressure", "temperature/humidity"),
    HUMIDITY("humidity", "temperature/humidity"),
    ATMOSPHERIC_PRESSURE("atmospheric_pressure", "temperature/pressure"),
    WIND_SPEED("wind_speed"),
    WIND_DIRECTION("wind_direction"),
    RAIN_AMOUNT("rain_amount"),

    TEMPERATURE_WATER,
    ACCELERATION("acceleration"),

    CAMERA("camera"),
    UNKNOWN;

    private String[] sensorTypes;

    private SensorType(String... sensorTypes) {
        this.sensorTypes = sensorTypes;
    }

    public static List<SensorType> from(Sensor sensor) {
        List<SensorType> result = new ArrayList<>();
        for (SensorType type : values()) {
            for (String sensorType : type.sensorTypes) {
                if (sensorType.equalsIgnoreCase(sensor.type)) {
                    result.add(type);
                    break;
                }
            }
        }
        if (result.isEmpty()) {
            result.add(UNKNOWN);
        }
        return result;
    }
}
