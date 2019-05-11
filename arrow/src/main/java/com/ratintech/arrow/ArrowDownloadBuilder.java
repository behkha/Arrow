package com.ratintech.arrow;

public class ArrowDownloadBuilder {

    String url;
    String directory;
    String tag;
    ArrowDownload.DownloadListener downloadListener;

    public ArrowDownloadBuilder() {
    }

    public ArrowDownloadBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public ArrowDownloadBuilder setDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    public ArrowDownloadBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ArrowDownloadBuilder setDownloadListener(ArrowDownload.DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    public ArrowDownload build() {
        if (url == null || url.isEmpty())
            throw new IllegalArgumentException("url can not be null or empty");

        if (directory == null || directory.isEmpty())
            throw new IllegalArgumentException("directory can not be null or empty");

        if (tag == null || tag.isEmpty())
            throw new IllegalArgumentException("tag can not be null or empty");
        return new ArrowDownload(this);
    }
}
