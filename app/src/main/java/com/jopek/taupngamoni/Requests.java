package com.jopek.taupngamoni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

class RequestImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Consumer<Bitmap> cb;

    public void setRemoteSrc(ImageView bmImage, String url) {
        this.bmImage = bmImage;
        this.execute(url);
    }

    public void getBitmap(String url, Consumer<Bitmap> cb) {
        this.cb = cb;
        this.execute(url);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new URL(urldisplay).openStream();
            Log.d("maks", "doInBackground: " + in.available());
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        if (cb == null)
            bmImage.setImageBitmap(result);
        else cb.accept(result);
    }
}

public class Requests extends AsyncTask<String, Integer, String> {
    Consumer<String> cb;
    Consumer<JSONObject> cbJson;
    boolean retJson = false;

    RequestImage requestImage = new RequestImage();

    public void get(String url, Consumer<String> cb) {
        this.cb = cb;
        this.execute(url);
    }

    public void getJson(String url, Consumer<JSONObject> cb) {
        this.cbJson = cb;
        this.retJson = true;
        this.execute(url);
    }

    public void setRemoteSrc(ImageView bmImage, String url) {
        requestImage.setRemoteSrc(bmImage, url);
    }

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            InputStream in = url.openStream();
//            Log.d("maks", "doInBackground: " + in.available());
//            bmp = BitmapFactory.decodeStream(in);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setDoOutput(true);
//            connection.setConnectTimeout(5000);
//            connection.setReadTimeout(5000);
//            connection.connect();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            return content;
        } catch (IOException exception) {
            exception.printStackTrace();
            return "ERROR";
        }
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here
        if (retJson) {
            try {
                cbJson.accept(new JSONObject(result));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            cb.accept(result);
        }
    }
}

class RequestsSync {
    public static void setBitmap(String urlStr, ImageView img) {
        try {
            URL url = new URL(urlStr);
            InputStream in = url.openStream();
            Bitmap bmp = BitmapFactory.decodeStream(in);
            img.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject get(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            return new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}