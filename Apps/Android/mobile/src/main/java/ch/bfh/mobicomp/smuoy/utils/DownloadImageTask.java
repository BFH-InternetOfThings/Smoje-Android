package ch.bfh.mobicomp.smuoy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {
    private String identifier;
    private ImageView imageView;
    private long cacheTimeInMilliSeconds = 900000; // 15 minutes by default?

    public DownloadImageTask(String identifier, ImageView imageView, long cacheTimeInSeconds) {
        this.identifier = identifier;
        this.imageView = imageView;
        this.cacheTimeInMilliSeconds = cacheTimeInSeconds * 1000;
    }

    protected Bitmap doInBackground(URL... urls) {
        try {
            File cacheFile = new File(imageView.getContext().getExternalCacheDir(), identifier);
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
        imageView.setImageBitmap(result);
    }
}