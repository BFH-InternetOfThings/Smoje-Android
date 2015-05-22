package ch.bfh.mobicomp.smuoy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import ch.bfh.mobicomp.smuoy.cards.*;
import ch.bfh.mobicomp.smuoy.entities.Sensor;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;

import static ch.bfh.mobicomp.smuoy.services.SmuoyService.smuoyService;

/**
 * A fragment representing a single Smuoy detail screen.
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        if (item == null && (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID))) {
            item = smuoyService.getSmuoy(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smuoy_detail, container, false);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.smuoy_detail);
        final LinearLayout layoutLeft;
        final LinearLayout layoutRight;

        if (layout == null) {
            layoutLeft = (LinearLayout) rootView.findViewById(R.id.smuoy_detail_left);
            layoutRight = (LinearLayout) rootView.findViewById(R.id.smuoy_detail_right);
        } else {
            layoutLeft = layout;
            layoutRight = layout;
        }

        if (item != null) {
            MapCardUpdater mapUpdater = new MapCardUpdater(getFragmentManager());
            mapUpdater.makeCard(inflater, layoutLeft);
            item.addUpdater(mapUpdater);

            item.setListener(new Smuoy.SensorListener() {
                @Override
                public void added(Sensor sensor) {
                    if (sensor.displayType >= 3) return;

                    CardUpdater updater = null;
                    switch (sensor.name) {
                        case "camera":
                            updater = new ImageCardUpdater();
                            updater.makeCard(inflater, layoutLeft);
                            break;
                        case "air_humidity":
                            updater = new DefaultCardUpdater(R.string.humidity);
                            updater.makeCard(inflater, layoutRight);
                            break;
                        case "air_temperature":
                            updater = new AirTemperatureCardUpdater();
                            updater.makeCard(inflater, layoutRight);
                            break;
                        case "air_athmosphericpressure":
                            updater = new AtmosphericPressureCardUpdater();
                            updater.makeCard(inflater, layoutRight);
                            break;
                        case "air_windspeed":
                            updater = new WindCardUpdater();
                            updater.makeCard(inflater, layoutRight);
                            break;
                        case "air_rainamount":
                            updater = new RainCardUpdater();
                            updater.makeCard(inflater, layoutRight);
                            break;
                        case "water_temperature":
                            updater = new WaterTemperatureCardUpdater();
                            updater.makeCard(inflater, layoutRight);
                            break;
                        // Ignore:
                        case "smoje_battery": // not shown
                        case "air_compass":
                        case "air_winddirection": // already handled with air_windspeed
                            break;
                        // Default cards, not specially handled
                        case "air_uvlight":
                        case "air_geiger":
                        case "water_salinity":
                        case "water_dissolvedoxygen":
                        case "water_drift":
                        default:
                            updater = new DefaultCardUpdater(sensor.name);
                            updater.makeCard(inflater, layoutRight);
                            break;
                    }
                    sensor.setUpdater(updater);
                }

            });
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (item != null) {
            activity.setTitle(item.name);
        }
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.app_name);
        super.onDetach();
    }

    public static SmuoyDetailFragment newInstance(Smuoy smuoy) {
        SmuoyDetailFragment fragment = new SmuoyDetailFragment();
        fragment.item = smuoy;
        return fragment;
    }

}
