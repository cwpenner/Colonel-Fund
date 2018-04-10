package com.colonelfund.colonelfund;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Image downloader class for profile and event pictures.
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    public ImageDownloadDelegate delegate = null;
    /**
     * Sets public interface for image downloader delegate.
     */
    public interface ImageDownloadDelegate {
        void imageDownloaded(Bitmap bitmap);
    }

    /**
     * Sets delegate for image downloader.
     *
     * @param delegate for image downloader
     */
    public ImageDownloader(ImageDownloadDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Overrides background process for getting bitmap.
     *
     * @param urls for bitmap.
     * @return bitmap of image.
     */
    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap map = null;
        for (String url : urls) {
            map = downloadImage(url);
        }
        return map;
    }

    /**
     * Sets a callback for completion of download.
     *
     * @param bitmap of an image.
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        delegate.imageDownloaded(bitmap);
    }

    /**
     * Downloads an image from url string.
     *
     * @param url of an image.
     * @return bitmap of an image.
     */
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

    /**
     * Gets http connection input stream for image.
     *
     * @param urlString for image.
     * @return input stream for an image connection.
     * @throws IOException for input stream of image connection.
     */
    private InputStream getHttpConnection(String urlString) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
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
