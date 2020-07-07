package work.kozh.request.retrofit.handler.download;

public interface ProgressDownloadListener {
    void onStart();

    void onDownloadProgress(long current, long total);

    void onFail(String errorInfo);

    void onDownloadSuccess();


}
