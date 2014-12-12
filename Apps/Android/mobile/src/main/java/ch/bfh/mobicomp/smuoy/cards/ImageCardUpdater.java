package ch.bfh.mobicomp.smuoy.cards;

import android.support.v7.widget.CardView;
import android.util.Log;
import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.utils.DownloadImageTask;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chris on 12.12.14.
 */
public class ImageCardUpdater extends CardUpdater {
    @Override
    protected void updateCard(Measurement measurement) {
        try {
            URL url = new URL(measurement.getValueString());
            new DownloadImageTask(measurement.smuoyId, card, 300).execute(url);
        } catch (MalformedURLException e) {
            Log.e("detail", e.getMessage(), e);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_image;
    }
}
