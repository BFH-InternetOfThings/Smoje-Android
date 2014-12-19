package ch.bfh.mobicomp.smuoy.cards;

import android.support.v4.app.FragmentManager;
import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.SmuoyMapFragment;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by chris on 12.12.14.
 */
public class MapCardUpdater extends CardUpdater {
    private final FragmentManager fragmentManager;

    public MapCardUpdater(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void updateCard(Measurement measurement) {
        String[] latlng = measurement.getString().split(";");
        double latitude = Double.parseDouble(latlng[0]);
        double longitude = Double.parseDouble(latlng[1]);
        LatLng location = new LatLng(latitude, longitude);
        GoogleMapOptions mapOptions = new GoogleMapOptions();
        mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL);
        mapOptions.camera(new CameraPosition(location, 12, 0, 0));

        SmuoyMapFragment mapFragment = SmuoyMapFragment.newInstance(new MarkerOptions().position(location));
        fragmentManager.beginTransaction().add(R.id.map_container, mapFragment).commit();
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_map;
    }
}
