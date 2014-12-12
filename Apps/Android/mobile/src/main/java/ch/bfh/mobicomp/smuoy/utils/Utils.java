package ch.bfh.mobicomp.smuoy.utils;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chris on 07.11.14.
 */
public class Utils {
    private final static DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static String str(JSONObject json, String field, String fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return o.toString();
            }
        } catch (JSONException e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON: " + json, e);
        }
        return fallback;
    }

    public static Date date(JSONObject json, String field, Date fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return ISO_DATE_FORMAT.parse(o.toString());
            }
        } catch (Exception e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON: " + json, e);
        }
        return fallback;
    }

    public static int num(JSONObject json, String field, int fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return Integer.parseInt(o.toString());
            }
        } catch (Exception e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON", e);
        }
        return fallback;
    }

    public static double dec(JSONObject json, String field, double fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return Double.parseDouble(o.toString());
            }
        } catch (Exception e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON", e);
        }
        return fallback;
    }

    private final static String[] DIRECTIONS = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

    public static String direction(double degrees) {
        double deg = (degrees % 360) + 360;

        for (int i = 0; ; i++) {
            if (deg < (11.25 + i * 22.5)) {
                return DIRECTIONS[i % 16];
            }
        }
    }

    public static String request(HttpClient httpClient, HttpUriRequest request) throws IOException {
        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            return out.toString();
        } else {
            // Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }
}
