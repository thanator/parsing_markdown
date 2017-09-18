package com.tan_ds.test_markdown;

import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tan-DS on 9/13/2017.
 */

public class DocReader extends AsyncTaskLoader <String>{

    private String myURL;

    public DocReader(Context context, String strUrl) {
        super(context);
        this.myURL = strUrl;
    }

    @Override
    public String loadInBackground() {
     String someShit = TryToDoIt(myURL);
        return someShit;
    }

    private String TryToDoIt (String urlStr){
        HttpURLConnection httpURLConnection = null;
        String someText = "";
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();
            switch (status){
                case 200:
                case 201:
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedInputStream inputStream = new BufferedInputStream(is, 1024*5);
                    File file = new File(getContext().getDir(Environment.DIRECTORY_DOWNLOADS, Context.MODE_PRIVATE) + "temp.txt");
                    if (file.exists()){
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buff = new byte[5*1024];
                    int len;
                    while ((len = inputStream.read(buff)) != -1){
                        outputStream.write(buff, 0, len);
                    }
                    outputStream.flush();
                    outputStream.close();
                    StringBuilder text = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                    someText = text.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return someText;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
