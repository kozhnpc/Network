package work.kozh.request.retrofit.handler.upload;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class RxUploader {

    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit retrofit;
    private ProgressUploadListener mListener;
    private String filePath;
    private String uploadUrl;

    public RxUploader(ProgressUploadListener listener, String filePath, String url) {
        mListener = listener;
        this.filePath = filePath;
        this.uploadUrl = url;

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(uploadUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public void upload(String filePath, Observer subscriber) {
        mListener.onStart();

        File file = new File(filePath);
        RequestBody body = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);

        retrofit.create(ProgressUploadService.class)
                .upload(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        
        mListener.onFinish();

    }
}
