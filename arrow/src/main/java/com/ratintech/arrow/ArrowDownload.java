package com.ratintech.arrow;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ArrowDownload extends AsyncTask<String , Integer , File> {

    private String url;
    private String directory;
    private String fileName;
    private String tag;
    private DownloadListener downloadListener;

    protected ArrowDownload() {

    }

    ArrowDownload(ArrowDownloadBuilder arrowDownloadBuilder){
        this.url = arrowDownloadBuilder.url;
        this.fileName = url.substring( url.lastIndexOf('/') + 1 );
        this.directory = arrowDownloadBuilder.directory;
        this.tag = arrowDownloadBuilder.tag;
        this.downloadListener = arrowDownloadBuilder.downloadListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.downloadListener != null)
            this.downloadListener.onDownloadStarted();
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (this.downloadListener != null)
            this.downloadListener.onDownloadFinished(file);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this.downloadListener.onDownloadProgress(values[0]);
    }

    @Override
    protected void onCancelled(File file) {
        super.onCancelled(file);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        removeFile( this.fileName );
    }

    private void removeFile(String fileName){
        File file = new File( this.directory + File.separator +  fileName );
        if (file.exists())
            file.delete();
    }

    public DownloadListener getDownloadListener(){
        return this.downloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener){
        this.downloadListener = downloadListener;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public String getTag(){
        return this.tag;
    }

    public boolean isDownloaded(){
        File file = new File( this.directory + File.separator +  fileName );
        return file.exists();
    }

    @Override
    protected File doInBackground(String... strings) {
        String url = this.url;
        String directory = this.directory;
        int count;
        if (isCancelled()) {
            if (this.downloadListener != null)
                this.downloadListener.onDownloadInterrupted();
        } else {

            try {
                File file = new File(directory);
                if (!file.exists())
                    file.mkdirs();
                URL url1 = new URL(url);
                URLConnection connection = url1.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                InputStream inputStream = new BufferedInputStream( connection.getInputStream() );
                OutputStream outputStream = new FileOutputStream( directory + File.separator  + this.fileName );
                byte[] data = new byte[1024];
                long total = 0;
                while ( (count = inputStream.read(data)) != -1 ){
                    if (isCancelled()){
                        if (this.downloadListener != null)
                            this.downloadListener.onDownloadInterrupted();
                        removeFile(this.fileName);
                        return null;
                    }
                    total += count;
                    publishProgress((int)((total*100)/lengthOfFile));
                    outputStream.write( data , 0 , count );
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                return new File( this.directory + File.separator +  fileName );

            } catch (Exception ex){
                ex.printStackTrace();
                if (this.downloadListener != null)
                    this.downloadListener.onDownloadInterrupted();
                removeFile(this.fileName);
            }

        }


        return null;
    }

    public interface DownloadListener {
        void onDownloadStarted();
        void onDownloadFinished(@Nullable File file);
        void onDownloadProgress(int progress);
        void onDownloadInterrupted();
    }



}
