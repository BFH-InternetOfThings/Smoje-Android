package ch.bfh.mobicomp.smuoy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.bfh.mobicomp.smuoy.dummy.DummyContent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import static ch.bfh.mobicomp.smuoy.dummy.DummyContent.*;


public class MapActivity extends ActionBarActivity {
    private final static LatLngBounds LAKE_BIEL = new LatLngBounds(
            new LatLng(47.034301, 7.060707),
            new LatLng(47.136459, 7.243011)
    );

    private MapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportActionBar().setIcon(R.drawable.ic_launcher);


        mapFragment = (MapFragment) getFragmentManager().findFragmentByTag("map");

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();

            getFragmentManager().beginTransaction()
                    .add(R.id.container, mapFragment, "map")
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {
            if (map == null) {
                map = mapFragment.getMap();
                if (map != null) {
                    map.setMyLocationEnabled(true);
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(LAKE_BIEL, 0));
                        }
                    });
                    for (Smuoy smuoy: ITEMS) {
                        for (SensorData sensor : smuoy.sensorData) {
                            if ("gps".equalsIgnoreCase(sensor.sensor)) {
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(10, 10))
                                        .title("Hello world"));
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_loading_map,
                            Toast.LENGTH_LONG).show();
                }
            }

        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
