package ch.bfh.mobicomp.smuoy.cards;

import android.widget.ImageView;
import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;

/**
 * Created by chris on 12.12.14.
 */
public class WaterTemperatureCardUpdater extends CardUpdater {
    @Override
    protected void updateCard(Measurement measurement) {
        double temperature = measurement.getValue();

        ImageView icon = (ImageView) card.findViewById(R.id.icon);
        setText(R.id.label, R.string.temperature_water);

        if (temperature < 10) {
            icon.setImageResource(R.drawable.ic_temperature_water_cold);
        } else if (temperature < 15) {
            icon.setImageResource(R.drawable.ic_temperature_water_warm);
        } else {
            icon.setImageResource(R.drawable.ic_temperature_water_hot);
        }
        setText(R.id.value, String.format("%1$.1f%2$s", temperature, "˚C"));
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_temperature;
    }
}
