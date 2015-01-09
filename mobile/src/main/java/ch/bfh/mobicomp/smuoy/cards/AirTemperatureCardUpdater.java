package ch.bfh.mobicomp.smuoy.cards;

import android.support.v7.widget.CardView;
import android.widget.ImageView;
import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;

/**
 * Created by chris on 12.12.14.
 */
public class AirTemperatureCardUpdater extends CardUpdater {
    @Override
    protected void updateCard(Measurement measurement) {
        double temp = measurement.getValue();

        ImageView icon = (ImageView) card.findViewById(R.id.icon);
        setText(R.id.label, R.string.temperature_air);

        if (temp < 15) {
            icon.setImageResource(R.drawable.ic_temperature_air_cold);
        } else if (temp < 25) {
            icon.setImageResource(R.drawable.ic_temperature_air_warm);
        } else {
            icon.setImageResource(R.drawable.ic_temperature_air_hot);
        }
        setText(R.id.value, String.format("%1$.1f%2$s", temp, "˚C"));
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_temperature;
    }
}
