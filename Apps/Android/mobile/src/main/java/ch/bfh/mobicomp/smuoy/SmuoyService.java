package ch.bfh.mobicomp.smuoy;

import android.os.AsyncTask;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import org.json.JSONArray;

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
    private static final String SERVICE_URL = "http://178.62.163.199/smoje/index.php/Measurement";
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

    private synchronized void updateSmuoys(List<Smuoy> newSmuoys) {
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

//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpResponse response = httpclient.execute(new HttpGet(params[0]));
//                    StatusLine statusLine = response.getStatusLine();
                if (true) { //statusLine.getStatusCode() == HttpStatus.SC_OK) {
//                        ByteArrayOutputStream out = new ByteArrayOutputStream();
//                        response.getEntity().writeTo(out);
//                        out.close();
                    String responseString = json; // out.toString();

                    JSONArray jsonArray = new JSONArray(responseString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        result.add(new Smuoy(jsonArray.getJSONObject(i)));
                    }
                } else {
                    //Closes the connection.
//                        response.getEntity().getContent().close();
//                        throw new IOException(statusLine.getReasonPhrase());
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Smuoy> smuoys) {
            updateSmuoys(smuoys);
            loading = false;
            for (SmuoyLoadedListener listener : listeners) {
                listener.onSmuoyListLoaded(smuoys);
            }
        }
    }

    private String json = "[\n" +
            "\t{\n" +
            "\t\t\"id\" : \"1\",\n" +
            "\t\t\"name\" : \"Ἄρτεμις 🎱\",\n" +
            "\t\t\"description\" : \"\",\n" +
            "\t\t\"sensors\" : [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\"name\" : \"gps\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 60, // seconds\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\t\"name\" : \"location\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\t\t\"name\" : \"lat\",\n" +
            "\t\t\t\t\t\t\"value\" : 47.102382,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°N\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.007+0100\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"1\",\n" +
            "\t\t\t\t\t\t\"name\" : \"lon\",\n" +
            "\t\t\t\t\t\t\"value\" : 7.196388,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°E\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.007+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"2\",\n" +
            "\t\t\t\t\"name\" : \"wind\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 120,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"2\",\n" +
            "\t\t\t\t\t\"name\" : \"wind\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"4\",\n" +
            "\t\t\t\t\t\t\"name\" : \"speed\",\n" +
            "\t\t\t\t\t\t\"value\" : 5.1,\n" +
            "\t\t\t\t\t\t\"unit\" : \"km/h\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.128+0100\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"4\",\n" +
            "\t\t\t\t\t\t\"name\" : \"direction\",\n" +
            "\t\t\t\t\t\t\"value\" : 136.916481,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:16:42.128+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\"name\" : \"air temperature\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 90,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\"name\" : \"temperature\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\t\"name\" : \"air temperature\",\n" +
            "\t\t\t\t\t\t\"value\" : 10.6128641024,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°C\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:15:12.064+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\"name\" : \"water temperature\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 90,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\"name\" : \"temperature\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\t\"name\" : \"water temperature\",\n" +
            "\t\t\t\t\t\t\"value\" : 12.012800641024,\n" +
            "\t\t\t\t\t\t\"unit\" : \"°C\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T14:15:12.640+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\" : \"4\",\n" +
            "\t\t\t\t\"name\" : \"selfie\",\n" +
            "\t\t\t\t\"description\" : \"\",\n" +
            "\t\t\t\t\"delay\" : 3600,\n" +
            "\t\t\t\t\"status\" : \"ok\",\n" +
            "\t\t\t\t\"type\" : {\n" +
            "\t\t\t\t\t\"id\" : \"3\",\n" +
            "\t\t\t\t\t\"name\" : \"image\",\n" +
            "\t\t\t\t\t\"description\" : \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"measurements\" : [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"id\" : \"5\",\n" +
            "\t\t\t\t\t\t\"name\" : \"image\",\n" +
            "\t\t\t\t\t\t\"value\" : \"http://www.fischerforum.ch/coppermine/albums/userpics/10002/070802_Bielersee_Ferien_04.jpg\",\n" +
            "\t\t\t\t\t\t\"unit\" : \"image/jpeg\",\n" +
            "\t\t\t\t\t\t\"timestamp\" : \"2014-11-07T13:21:12.640+0100\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t}\n" +
            "\t\t\t// more...?\n" +
            "\t\t]\n" +
            "\t}\n" +
            "\t// more...?\n" +
            "]";
}
