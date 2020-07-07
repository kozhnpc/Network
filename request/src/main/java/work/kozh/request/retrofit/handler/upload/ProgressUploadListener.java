package work.kozh.request.retrofit.handler.upload;

public interface ProgressUploadListener {
    void onStart();

    void onUploadProgress(long current, long total);

    void onFinish();

    void onFail(String errorInfo);

}
