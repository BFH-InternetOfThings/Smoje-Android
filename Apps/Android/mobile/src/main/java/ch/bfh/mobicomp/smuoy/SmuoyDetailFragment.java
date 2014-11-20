package ch.bfh.mobicomp.smuoy;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ch.bfh.mobicomp.smuoy.entities.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static ch.bfh.mobicomp.smuoy.SmuoyService.smuoyService;
import static ch.bfh.mobicomp.smuoy.Utils.direction;

/**
 * A fragment representing a single Smuoy detail screen.
 * This fragment is either contained in a {@link SmuoyListActivity}
 * in two-pane mode (on tablets) or a {@link SmuoyDetailActivity}
 * on handsets.
 */
public class SmuoyDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Smuoy item;
    private MapFragment mapFragment;
    private MarkerOptions mapMarker;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SmuoyDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            item = smuoyService.getSmuoy(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.fragment_smuoy_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (item != null) {
            getActivity().setTitle(item.name);
            for (Sensor sensor : item.sensors) {
                addCard(inflater, rootView, sensor.latestData);
            }
        }

        return rootView;
    }

    private void addCard(LayoutInflater inflater, ViewGroup parentView, CharSequence text) {
        CardView card = (CardView) inflater.inflate(R.layout.data_card_basic, parentView, false);

        TextView titleText = (TextView) card.findViewById(R.id.text);
        titleText.setText(text);

        parentView.addView(card);
    }

    private void addCard(LayoutInflater inflater, final ViewGroup parentView, Measurement measurement) {
        CardView card;
        if (measurement instanceof WindMeasurement) {
            card = getCard(inflater, parentView, R.layout.data_card_wind);
            WindMeasurement windMeasurement = (WindMeasurement) measurement;
            setText(card, R.id.speed, String.format("%1$.2fkm/h", windMeasurement.speed));
            setText(card, R.id.direction, direction(windMeasurement.direction));
        } else if (measurement instanceof ImageMeasurement) {
            card = getCard(inflater, parentView, R.layout.data_card_image);
            new DownloadImageTask((ImageView) card.findViewById(R.id.image)).execute(((ImageMeasurement) measurement).url);
        } else if (measurement instanceof GpsMeasurement) {
            card = getCard(inflater, parentView, R.layout.data_card_map);

            GpsMeasurement gpsMeasurement = (GpsMeasurement) measurement;

            GoogleMapOptions mapOptions = new GoogleMapOptions();
            mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL);
            mapOptions.camera(new CameraPosition(new LatLng(gpsMeasurement.lat, gpsMeasurement.lon), 12, 0, 0));

            mapFragment = MapFragment.newInstance(mapOptions);
            getFragmentManager().beginTransaction().add(R.id.map_container, mapFragment).commit();

            mapMarker = new MarkerOptions().position(new LatLng(gpsMeasurement.lat, gpsMeasurement.lon));
        } else {
            card = getCard(inflater, parentView, R.layout.data_card_basic);
            setText(card, R.id.text, measurement.toString());
        }
        parentView.addView(card);
    }

    private CardView getCard(LayoutInflater inflater, ViewGroup parentView, int id) {
        return (CardView) inflater.inflate(id, parentView, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mapFragment == null || mapMarker == null) {
            return;
        }

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == ConnectionResult.SUCCESS) {
            GoogleMap map = mapFragment.getMap();
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setAllGesturesEnabled(false);
                map.addMarker(mapMarker);
            } else {
                Toast.makeText(getActivity(),
                        R.string.error_loading_map,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 1);
        }
    }

    private void setText(View parent, int id, CharSequence text) {
        ((TextView) parent.findViewById(id)).setText(text);
    }

}
