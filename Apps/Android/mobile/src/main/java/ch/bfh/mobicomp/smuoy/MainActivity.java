package ch.bfh.mobicomp.smuoy;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.bfh.mobicomp.smuoy.entities.GpsMeasurement;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.entities.Sensor;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import static ch.bfh.mobicomp.smuoy.SmuoyService.smuoyService;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final static LatLngBounds LAKE_BIEL = new LatLngBounds(
            new LatLng(47.034301, 7.060707),
            new LatLng(47.136459, 7.243011)
    );

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;
    private Map<String, Smuoy> markerSmuoyMap = new HashMap<>();
    private MapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (savedInstanceState == null) {
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
                    for (Smuoy smuoy : smuoyService.getSmuoys()) {
                        for (Sensor sensor : smuoy.sensors) {
                            Measurement data = sensor.latestData;
                            if (data instanceof GpsMeasurement) {
                                GpsMeasurement gpsData = (GpsMeasurement) data;
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(gpsData.lat, gpsData.lon))
                                        .title(smuoy.name));
                                marker.showInfoWindow();
                                markerSmuoyMap.put(marker.getId(), smuoy);
                                break; // even if there's more than one GPS measurement, we're not interested
                            }
                        }
                    }
                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Smuoy smuoy = markerSmuoyMap.get(marker.getId());

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, SmuoyDetailFragment.newInstance(smuoy))
                                    .addToBackStack(smuoy.id)
                                    .commit();
                        }
                    });
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
    public void onNavigationDrawerItemSelected(Smuoy smuoy) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, SmuoyDetailFragment.newInstance(smuoy))
                .addToBackStack(smuoy.id)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
