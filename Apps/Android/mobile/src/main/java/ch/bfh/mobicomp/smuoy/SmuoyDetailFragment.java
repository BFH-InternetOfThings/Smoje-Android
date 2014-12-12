package ch.bfh.mobicomp.smuoy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.entities.SensorType;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;
import ch.bfh.mobicomp.smuoy.utils.DownloadImageTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static ch.bfh.mobicomp.smuoy.MeasurementService.measurementService;
import static ch.bfh.mobicomp.smuoy.SmuoyService.smuoyService;
import static ch.bfh.mobicomp.smuoy.utils.Utils.direction;

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

    private CharSequence originalTitle;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smuoy_detail, container, false);
        LinearLayout layoutLeft = (LinearLayout) rootView.findViewById(R.id.smuoy_detail);
        LinearLayout layoutRight;

        if (layoutLeft == null) {
            layoutLeft = (LinearLayout) rootView.findViewById(R.id.smuoy_detail_left);
            layoutRight = (LinearLayout) rootView.findViewById(R.id.smuoy_detail_right);
        } else {
            layoutRight = layoutLeft;
        }

        if (item != null) {
            // Map
            LatLng location = measurementService.getLocation(item);
            if (location != null) {
                CardView card = CardType.MAP.getCard(inflater, layoutLeft);
                GoogleMapOptions mapOptions = new GoogleMapOptions();
                mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL);
                mapOptions.camera(new CameraPosition(location, 12, 0, 0));

                SmuoyMapFragment mapFragment = SmuoyMapFragment.newInstance(new MarkerOptions().position(location));
                getFragmentManager().beginTransaction().add(R.id.map_container, mapFragment).commit();
            }

            // Image
            List<Measurement> images = measurementService.getMeasurements(item, SensorType.CAMERA);
            for (Measurement image : images) {
                try {
                    URL url = new URL(image.getValueString());
                    CardView card = CardType.IMAGE.getCard(inflater, layoutLeft);
                    new DownloadImageTask(item.id, card, 300).execute(url);
                } catch (MalformedURLException e) {
                    Log.e("detail", e.getMessage(), e);
                }
            }

            // Wind
            List<Measurement> windSpeed = measurementService.getMeasurements(item, SensorType.WIND_SPEED);
            List<Measurement> windDirection = measurementService.getMeasurements(item, SensorType.WIND_DIRECTION);
            // TODO: convert to km/h
            if (!windSpeed.isEmpty() && !windDirection.isEmpty()) {
                CardView card = CardType.WIND.getCard(inflater, layoutRight);
                setText(card, R.id.speed, String.format("%1$.2fm/s", windSpeed.get(0).getValueDecimal()));
                setText(card, R.id.direction, direction(windDirection.get(0).getValueDecimal()));
            }

            // Temperature
            List<Measurement> airTemperature = measurementService.getMeasurements(item, SensorType.TEMPERATURE_AIR);
            if (!airTemperature.isEmpty()) {
                double temp = airTemperature.get(0).getValueDecimal();

                CardView card = CardType.TEMPERATURE.getCard(inflater, layoutRight);
                ImageView icon = (ImageView) card.findViewById(R.id.icon);
                setText(card, R.id.label, R.string.temperature_air);

                if (temp < 15) {
                    icon.setImageResource(R.drawable.ic_temperature_air_cold);
                } else if (temp < 25) {
                    icon.setImageResource(R.drawable.ic_temperature_air_warm);
                } else {
                    icon.setImageResource(R.drawable.ic_temperature_air_hot);
                }
                setText(card, R.id.rain_amount, String.format("%1$.1f%2$s", temp, "˚C"));
            }

            List<Measurement> waterTemperature = measurementService.getMeasurements(item, SensorType.TEMPERATURE_WATER);
            if (!waterTemperature.isEmpty()) {
                double temp = waterTemperature.get(0).getValueDecimal();

                CardView card = CardType.TEMPERATURE.getCard(inflater, layoutRight);
                ImageView icon = (ImageView) card.findViewById(R.id.icon);
                setText(card, R.id.label, R.string.temperature_water);

                if (temp < 10) {
                    icon.setImageResource(R.drawable.ic_temperature_water_cold);
                } else if (temp < 15) {
                    icon.setImageResource(R.drawable.ic_temperature_water_warm);
                } else {
                    icon.setImageResource(R.drawable.ic_temperature_water_hot);
                }
                setText(card, R.id.rain_amount, String.format("%1$.1f%2$s", temp, "˚C"));
            }

            // Rain
            List<Measurement> rain = measurementService.getMeasurements(item, SensorType.RAIN_AMOUNT);
            if (!rain.isEmpty()) {
                double rainAmount = rain.get(0).getValueDecimal();
                CardView card = CardType.RAIN.getCard(inflater, layoutRight);
                setText(card, R.id.rain_amount, String.format("%1$.1f%2$s", rainAmount, " mm"));
            }

            // Atmospheric Pressure
            List<Measurement> atmosphericPressure = measurementService.getMeasurements(item, SensorType.ATMOSPHERIC_PRESSURE);
            if (!atmosphericPressure.isEmpty()) {
                double pressure = atmosphericPressure.get(0).getValueDecimal();
                CardView card = CardType.ATMOSPHERIC_PRESSURE.getCard(inflater, layoutRight);
                setText(card, R.id.label, R.string.atmospheric_pressure);
                setText(card, R.id.value, String.format("%1$.1f%2$s", pressure * 10, " hPa"));
            }

            // Humidity
            List<Measurement> humidity = measurementService.getMeasurements(item, SensorType.HUMIDITY);
            if (!humidity.isEmpty()) {
                double relativeHumidity = humidity.get(0).getValueDecimal();
                CardView card = CardType.HUMIDITY.getCard(inflater, layoutRight);
                setText(card, R.id.label, R.string.humidity);
                setText(card, R.id.value, String.format("%1$.1f%2$s", relativeHumidity, "%"));
            }

            // Other
            for (SensorType type : SensorType.values()) {
                switch (type) {
                    case POSITION:
                    case CAMERA:
                    case WIND_SPEED:
                    case WIND_DIRECTION:
                    case TEMPERATURE_AIR:
                    case TEMPERATURE_WATER:
                    case RAIN_AMOUNT:
                    case ATMOSPHERIC_PRESSURE:
                    case HUMIDITY:
                        continue;
                    default:
                        List<Measurement> measurements = measurementService.getMeasurements(item, type);
                        for (Measurement measurement : measurements) {
                            CardView card = CardType.OTHER.getCard(inflater, layoutLeft);
                            setText(card, R.id.label, measurement.getName());
                            setText(card, R.id.value, measurement.toString());
                        }
                }
            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (item != null) {
            originalTitle = activity.getTitle();
            Log.d("detailFragment", "" + originalTitle);
            activity.setTitle(item.name);
        }
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(originalTitle);
        super.onDetach();
    }

    private void setText(View parent, int id, CharSequence text) {
        ((TextView) parent.findViewById(id)).setText(text);
    }

    private void setText(View parent, int id, int textId) {
        ((TextView) parent.findViewById(id)).setText(textId);
    }

    public static SmuoyDetailFragment newInstance(Smuoy smuoy) {
        SmuoyDetailFragment fragment = new SmuoyDetailFragment();
        fragment.item = smuoy;
        return fragment;
    }

    private static enum CardType {
        IMAGE(R.layout.data_card_image),
        MAP(R.layout.data_card_map),
        TEMPERATURE(R.layout.data_card_temperature),
        WIND(R.layout.data_card_wind),
        RAIN(R.layout.data_card_rain),
        ATMOSPHERIC_PRESSURE(R.layout.data_card_basic),
        HUMIDITY(R.layout.data_card_basic),
        OTHER(R.layout.data_card_basic);

        private int layout;

        private CardType(int layout) {
            this.layout = layout;
        }

        private CardView getCard(LayoutInflater inflater, ViewGroup parentView) {
            CardView cardView = (CardView) inflater.inflate(layout, parentView, false);
            parentView.addView(cardView);
            return cardView;
        }
    }
}
