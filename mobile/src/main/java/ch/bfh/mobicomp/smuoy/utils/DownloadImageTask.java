package ch.bfh.mobicomp.smuoy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import ch.bfh.mobicomp.smuoy.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {
    private final String identifier;
    private final CardView cardView;
    private final ImageView imageView;
    private long cacheTimeInMilliSeconds = 900000; // 15 minutes by default?

    public DownloadImageTask(String identifier, ImageView imageView, long cacheTimeInSeconds) {
        this.identifier = identifier;
        this.cardView = null;
        this.imageView = imageView;
        this.cacheTimeInMilliSeconds = cacheTimeInSeconds * 1000;
    }

    public DownloadImageTask(String identifier, CardView cardView, long cacheTimeInSeconds) {
        this.identifier = identifier;
        this.cardView = cardView;
        cardView.setVisibility(View.GONE);
        this.imageView = (ImageView) cardView.findViewById(R.id.image);
        this.cacheTimeInMilliSeconds = cacheTimeInSeconds * 1000;
    }

    protected Bitmap doInBackground(URL... urls) {
        try {
            File cacheFile = new File(imageView.getContext().getCacheDir(), identifier);
            if (!cacheFile.exists() || cacheFile.lastModified() < System.currentTimeMillis() - cacheTimeInMilliSeconds) {
                InputStream in = urls[0].openStream();
                FileOutputStream out = new FileOutputStream(cacheFile);
                byte[] buf = new byte[1024];
                int r = 0;
                while ((r = in.read(buf)) > 0) {
                    out.write(buf, 0, r);
                }
            }
            return BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            imageView.setImageBitmap(result);
            if (cardView != null) {
                cardView.setVisibility(View.VISIBLE);
            }
        }
    }
}