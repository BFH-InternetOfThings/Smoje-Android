package ch.bfh.mobicomp.smuoy;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import ch.bfh.mobicomp.smuoy.entities.GpsMeasurement;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.entities.Sensor;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
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

    private final Map<String, Smuoy> markerSmuoyMap = new HashMap<>();
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
        ((NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer)).setUp(
                R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.setRetainInstance(true);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mapFragment, "map")
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {
            if (mapFragment == null) {
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("map");
            }
            if (map == null && mapFragment != null) {
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
                            showDetail(markerSmuoyMap.get(marker.getId()));
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
        showDetail(smuoy);
    }

    private void showDetail(Smuoy smuoy) {
        getSupportFragmentManager().beginTransaction()
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

}
