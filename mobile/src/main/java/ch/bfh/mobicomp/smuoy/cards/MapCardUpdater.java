package ch.bfh.mobicomp.smuoy.cards;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.SmuoyMapFragment;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static ch.bfh.mobicomp.smuoy.entities.Smuoy.LocationUpdater;

/**
 * Created by chris on 12.12.14.
 */
public class MapCardUpdater extends CardUpdater implements LocationUpdater {
    private final FragmentManager fragmentManager;
    private SmuoyMapFragment mapFragment;

    public MapCardUpdater(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void updateCard(Measurement measurement) {
        String[] latlng = measurement.getString().split(";");
        double latitude = Double.parseDouble(latlng[0]);
        double longitude = Double.parseDouble(latlng[1]);
        LatLng location = new LatLng(latitude, longitude);
        updateLocation(location);
    }

    public void update(final LatLng location) {
        if (card != null) {
            if (card.getHandler() == null) {
                card.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        updateLocation(location);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {

                    }
                });
            } else {
                card.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateLocation(location);
                    }
                });
            }
        }
    }

    protected void updateLocation(LatLng location) {
        if (mapFragment == null) {
            SmuoyMapFragment mapFragment = SmuoyMapFragment.newInstance(new MarkerOptions().position(location));
            fragmentManager.beginTransaction().add(R.id.map_container, mapFragment).commit();
        } else {
            mapFragment.updateMarker(location);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_map;
    }

    @Override
    public void makeCard(LayoutInflater inflater, ViewGroup parentView) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(inflater.getContext());
        if (resultCode == ConnectionResult.SUCCESS) {
            super.makeCard(inflater, parentView);
        }
    }
}
