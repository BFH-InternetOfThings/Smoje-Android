package ch.bfh.mobicomp.smuoy.services;

import android.os.AsyncTask;
import android.util.Log;
import ch.bfh.mobicomp.smuoy.entities.Sensor;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import ch.bfh.mobicomp.smuoy.utils.Utils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static ch.bfh.mobicomp.smuoy.services.MeasurementService.measurementService;
import static java.lang.System.currentTimeMillis;

/**
 * SmuoyService asynchronously loads Smuoys from the REST web service (this part is still to do).
 * To get smuoys, register a SmuoyLoadedListener and call {@link #loadSmuoys(SmuoyService.SmuoyLoadedListener)}.
 */
public class SmuoyService {
    private static final long MIN_TIME_BETWEEN_REQUESTS = 10000;
    private static final String SERVICE_URL = "http://smoje.ch/smoje/index.php/stations/";
    public static final SmuoyService smuoyService = new SmuoyService();

    private volatile boolean loading = false;
    private List<SmuoyLoadedListener> listeners = new LinkedList<>();

    private TreeMap<String, Smuoy> loadedSmuoys = new TreeMap<>();

    private long lastExecution;

    private SmuoyService() {
    }

    public synchronized void loadSmuoys(SmuoyLoadedListener listener) {
        listeners.add(listener);
        if (loading) return;

        if (currentTimeMillis() - lastExecution < MIN_TIME_BETWEEN_REQUESTS) {
            listener.onSmuoyListLoaded(loadedSmuoys.values());
            return;
        }
        loading = true;
        new LoadSmuoysTask().execute();
        lastExecution = currentTimeMillis();
    }

    private void updateSmuoys(List<Smuoy> newSmuoys) {
        if (newSmuoys == null) return;

        loadedSmuoys.clear();
        for (Smuoy smuoy : newSmuoys) {
            loadedSmuoys.put(smuoy.id, smuoy);
        }
    }

    public Smuoy getSmuoy(String id) {
        return loadedSmuoys.get(id);
    }

    public static interface SmuoyLoadedListener {
        /**
         * This callback method will be executed on the GUI thread.
         */
        public void onSmuoyListLoaded(Collection<Smuoy> smuoys);
    }

    private class LoadSmuoysTask extends AsyncTask<Void, Void, List<Smuoy>> {
        private HttpClient httpClient = new DefaultHttpClient();

        @Override
        protected List<Smuoy> doInBackground(Void... params) {
            try {
                List<Smuoy> result = new LinkedList<>();

                JSONObject jsonObject = new JSONObject(Utils.request(httpClient, new HttpGet(SERVICE_URL)));
                JSONArray jsonArray = jsonObject.getJSONArray("stations");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Smuoy smuoy = new Smuoy(jsonArray.getJSONObject(i));
                    result.add(smuoy);
                    measurementService.registerSmuoy(smuoy);
                    loadSensors(smuoy);
                }
                return result;
            } catch (Exception e) {
                Log.e("smuoyService", e.getMessage(), e);
                return null;
            }
        }

        private void loadSensors(Smuoy smuoy) {
            try {
                JSONObject jsonObject = new JSONObject(Utils.request(httpClient, new HttpGet(SERVICE_URL + smuoy.id + "/sensors/")));
                JSONArray jsonArray = jsonObject.getJSONArray("sensors");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Sensor sensor = new Sensor(jsonArray.getJSONObject(i));
                    smuoy.addSensor(sensor);
                    measurementService.registerSensor(smuoy, sensor);
                }
            } catch (Exception e) {
                Log.e("smuoyService", e.getMessage(), e);
            }
        }

        @Override
        protected void onPostExecute(List<Smuoy> smuoys) {
            if (smuoys == null)
                return;
            updateSmuoys(smuoys);
            loading = false;
            for (SmuoyLoadedListener listener : listeners) {
                listener.onSmuoyListLoaded(smuoys);
            }
        }
    }
}
