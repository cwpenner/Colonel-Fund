package com.colonelfund.colonelfund;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    public interface ImageDownloadDelegate {
        void imageDownloaded(Bitmap bitmap);
    }

    public ImageDownloadDelegate delegate = null;

    public ImageDownloader(ImageDownloadDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap map = null;
        for (String url : urls) {
            map = downloadImage(url);
        }

        return map;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        delegate.imageDownloaded(bitmap);
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private InputStream getHttpConnection(String urlString) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);

        try {
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stream;
    }

}
