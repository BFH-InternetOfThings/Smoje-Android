package ch.bfh.mobicomp.smuoy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {
    ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(URL... urls) {
        try {
            return BitmapFactory.decodeStream(urls[0].openStream());
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}