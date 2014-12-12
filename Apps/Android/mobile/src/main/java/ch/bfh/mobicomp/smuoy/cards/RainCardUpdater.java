package ch.bfh.mobicomp.smuoy.cards;

import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;

/**
 * Created by chris on 12.12.14.
 */
public class RainCardUpdater extends CardUpdater {
    @Override
    protected void updateCard(Measurement measurement) {
        setText(R.id.value, String.format("%1$.1f%2$s", measurement.getValueDecimal(), " mm"));
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_rain;
    }
}
