package ch.bfh.mobicomp.smuoy.cards;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.bfh.mobicomp.smuoy.R;
import ch.bfh.mobicomp.smuoy.entities.Measurement;
import ch.bfh.mobicomp.smuoy.utils.DownloadImageTask;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chris on 12.12.14.
 */
public class ImageCardUpdater extends CardUpdater {
    private static final String IMAGE_BASE_URL = "http://smoje.ch/img/";

    @Override
    protected void updateCard(Measurement measurement) {
        try {
            URL url = new URL(IMAGE_BASE_URL + measurement.getString().replace(" ", "%20"));
            new DownloadImageTask(measurement.smuoyId, card, 300).execute(url);
        } catch (MalformedURLException e) {
            Log.e("detail", e.getMessage(), e);
        }
    }

    @Override
    public void makeCard(LayoutInflater inflater, ViewGroup parentView) {
        card = (CardView) inflater.inflate(getLayout(), parentView, false);
        card.setVisibility(View.GONE);
        parentView.addView(card);
    }

    @Override
    protected int getLayout() {
        return R.layout.data_card_image;
    }
}
