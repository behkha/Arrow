# Arrow
Arrow is a library to make file download progress very easy.


### Usage
First add jitpack to your projects build.gradle file

```gradle
allprojects {
   	repositories {
   		...
   		maven { url "https://jitpack.io" }
   	}
}
```

Then add the dependency in your android app module's `build.gradle` file.

```gradle
dependencies {
    implementation 'com.github.Behkha:Arrow:1.0.0'
}
```

### Setup

``` java
Arrow.download(new ArrowDownloadBuilder()
                .setUrl( "file url" )
                .setDirectory( "directory to save" )
                .setTag( "tag for download task" )
                .setDownloadListener(new ArrowDownload.DownloadListener() {
                    @Override
                    public void onDownloadStarted() {
                        
                    }

                    @Override
                    public void onDownloadFinished(@Nullable File file) {
                        
                    }

                    @Override
                    public void onDownloadProgress(int progress) {
                        
                    }

                    @Override
                    public void onDownloadInterrupted() {
                        
                    }
                })
                .build());
```
