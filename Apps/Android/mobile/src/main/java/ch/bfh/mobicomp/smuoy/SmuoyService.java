package ch.bfh.mobicomp.smuoy;

import android.os.AsyncTask;
import android.util.Log;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static java.lang.System.currentTimeMillis;

/**
 * SmuoyService asynchronously loads Smuoys from the REST web service (this part is still to do).
 * To get smuoys, register a SmuoyLoadedListener and call {@link #loadSmuoys(ch.bfh.mobicomp.smuoy.SmuoyService.SmuoyLoadedListener)}.
 */
public class SmuoyService {
    private static final long MIN_TIME_BETWEEN_REQUESTS = 10000;
    private static final String SERVICE_URL = "http://178.62.163.199/smoje/index.php/Measurements";
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
        new LoadSmuoysTask().execute(SERVICE_URL);
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

    private class LoadSmuoysTask extends AsyncTask<String, Void, List<Smuoy>> {
        @Override
        protected List<Smuoy> doInBackground(String... params) {
            try {
                List<Smuoy> result = new LinkedList<>();

                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(new HttpGet(params[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    String responseString = out.toString();

                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray jsonArray = jsonObject.getJSONArray("stations");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        result.add(new Smuoy(jsonArray.getJSONObject(i)));
                    }
                } else {
                    // Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
                return result;
            } catch (Exception e) {
                Log.e("smuoyService", e.getMessage(), e);
                e.printStackTrace();
                return null;
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
