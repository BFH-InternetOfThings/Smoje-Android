package ch.bfh.mobicomp.smuoy.cards;

import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;

import static ch.bfh.mobicomp.smuoy.utils.Utils.direction;

/**
 * Created by chris on 12.12.14.
 */
public class WindCardUpdater extends CardUpdater {
    @Override
    protected void updateCard(Measurement measurement) {
        switch (measurement.getType()) {
            case "air_windspeed":
                setText(R.id.speed_value, String.format("%1$.2fm/s", measurement.getValueDecimal()));
                break;
            case "air_winddirection":
                setText(R.id.direction_value, direction(measurement.getValueDecimal()));
                break;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_wind;
    }
}
