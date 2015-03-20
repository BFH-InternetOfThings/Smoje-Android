package ch.bfh.mobicomp.smuoy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by chris on 28.11.14.
 */
public class SmuoyMapFragment extends Fragment {
    private SupportMapFragment fragment;
    private GoogleMap map;
    private MarkerOptions markerOptions;
    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_with_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == ConnectionResult.SUCCESS) {
            FragmentManager fm = getChildFragmentManager();
            fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
            if (fragment == null) {
                GoogleMapOptions mapOptions = new GoogleMapOptions();
                mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL);
                mapOptions.camera(new CameraPosition(markerOptions.getPosition(), 12, 0, 0));

                fragment = SupportMapFragment.newInstance(mapOptions);
                fm.beginTransaction().add(R.id.map_container, fragment).commit();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null && fragment != null) {
            map = fragment.getMap();
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setAllGesturesEnabled(false);
            marker = map.addMarker(markerOptions);
        }
    }

    public void updateMarker(LatLng location) {
        markerOptions.position(location);
        if (marker != null) {
            marker.setPosition(location);
            map.animateCamera(CameraUpdateFactory.newLatLng(location));
        }
    }

    public static SmuoyMapFragment newInstance(MarkerOptions marker) {
        SmuoyMapFragment f = new SmuoyMapFragment();
        f.setRetainInstance(true);
        f.markerOptions = marker;
        return f;
    }
}
