package ch.bfh.mobicomp.smuoy.cards;

import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;

/**
 * Created by chris on 12.12.14.
 */
public class DefaultCardUpdater extends CardUpdater {
    private String label;
    private int labelId;

    public DefaultCardUpdater(String label) {
        this.label = label;
    }

    public DefaultCardUpdater(int labelId) {
        this.labelId = labelId;
    }

    @Override
    protected void updateCard(Measurement measurement) {
        if (label != null) {
            setText(R.id.label, label);
        } else {
            setText(R.id.label, R.string.humidity);
        }
        setText(R.id.value, String.format("%1$.1f%2$s", measurement.getValue(), "%"));
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_basic;
    }
}
