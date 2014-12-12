package ch.bfh.mobicomp.smuoy.cards;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.bfh.mobicomp.smuoy.entities.Measurement;

public abstract class CardUpdater {
    protected CardView card;

    public void update(final Measurement measurement) {
        if (card != null) { // TODO: check if card can be updated, else set to null
            card.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    updateCard(measurement);
                }
            });
        }
    }

    protected abstract void updateCard(Measurement measurement);

    protected abstract int getLayout();

    public void makeCard(LayoutInflater inflater, ViewGroup parentView) {
        card = (CardView) inflater.inflate(getLayout(), parentView, false);
        parentView.addView(card);
    }

    protected void setText(int id, CharSequence text) {
        ((TextView) card.findViewById(id)).setText(text);
    }

    protected void setText(int id, int textId) {
        ((TextView) card.findViewById(id)).setText(textId);
    }
}
