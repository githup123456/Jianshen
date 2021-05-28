package com.shyj.jianshen.update;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadFile extends AsyncTask<String, Integer, String> {

    private String urlString;
    private String filePath;

    public DownloadFile(String url, String filePath){
        this.urlString = url;
        this.filePath = filePath;
    }

    @Override
    protected String doInBackground(String... urlParams) {
        int count;
        try {
            URL url = new URL(urlString);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conexion.getContentLength();

            // downlod the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int)(total*100/lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            if (downloadListener!=null){
                downloadListener.onFail(e);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (downloadListener!=null){
            downloadListener.onProgress(values[0]);
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (downloadListener!=null){
            downloadListener.onFinishDownload();
        }
    }

    public DownloadListener downloadListener;

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
}

