package work.kozh.request.retrofit.handler.download;

import android.app.Activity;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import work.kozh.request.retrofit.network.BaseObserver;

public class RxDownloader {

    private static final String TAG = "RxDownloader";
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit retrofit;
    private ProgressDownloadListener listener;
    private String downloadUrl;
    private Activity mActivity;
    private CompositeDisposable mDisposables;

    //这是针对Rxfragment的下载  通用下载
    public RxDownloader(String url, ProgressDownloadListener listener, Activity activity) {
        this.downloadUrl = url;
        this.listener = listener;
        this.mActivity = activity;

        mDisposables = new CompositeDisposable();

        ProgressDownloadInterceptor interceptor = new ProgressDownloadInterceptor(listener);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(downloadUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    //这是针对RxAppComActivity的下载
//    public RxDownloader(String url, ProgressDownloadListener listener, RxAppCompatActivity activity) {
//        this.downloadUrl = url;
//        this.listener = listener;
//        this.mActivity = activity;
//
//        ProgressDownloadInterceptor interceptor = new ProgressDownloadInterceptor(listener);
//
//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .retryOnConnectionFailure(true)
//                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl(downloadUrl)
//                .client(httpClient)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//
//    }

    /**
     * 下载  针对RxAppCompatActivity
     */
    public void downloadInRxAppCompatActivity(String url, final String filePath, Observer subscriber) {
        listener.onStart();
        // subscribeOn()改变调用它之前代码的线程
        // observeOn()改变调用它之后代码的线程
        retrofit.create(ProgressDownloadService.class)
                .download(url)
//                .compose(ProgressObserverUtils.<ResponseBody>applyProgressBar(mActivity,"下载中..."))
//                .compose(RxUtil.<ResponseBody>rxSchedulerHelper(mActivity))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }

                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream, filePath);
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 下载  针对Rxfragment
     */
    public void download(String url, final String filePath) {
        listener.onStart();
        // subscribeOn()改变调用它之前代码的线程
        // observeOn()改变调用它之后代码的线程
        retrofit.create(ProgressDownloadService.class)
                .download(url)
//                .compose(ProgressObserverUtils.<ResponseBody>applyProgressBar(mActivity))
//                .compose(RxUtil.<ResponseBody>rxSchedulerHelper(mRxFragment))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }

                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream, filePath);
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }


    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();
        } catch (FileNotFoundException e) {
            listener.onFail("文件不存在！");
        } catch (IOException e) {
            listener.onFail("读取文件错误！");
        }
    }


    private Observer getObserver() {
        return new BaseObserver() {

            @Override
            public void onSubscribe(Disposable d) {
                mDisposables.add(d);
            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onFailure(Throwable e, String msg_Error) {
                listener.onFail(msg_Error);
                mDisposables.clear();
            }

            @Override
            public void onSuccess(Object data) {
//                listener.onDownloadSuccess();
            }



            @Override
            public void onComplete() {
                listener.onDownloadSuccess();
                mDisposables.clear();
            }
        };
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        mDisposables.clear();
    }

}
