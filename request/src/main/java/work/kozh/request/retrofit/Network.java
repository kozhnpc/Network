package work.kozh.request.retrofit;

import android.app.Activity;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import work.kozh.request.retrofit.network.BaseObserver;
import work.kozh.request.retrofit.network.MyUrl;
import work.kozh.request.retrofit.network.RetrofitService;
import work.kozh.request.retrofit.network.loading.ProgressObserverUtils;

/**
 * 再次封装网络请求框架
 */

public class Network {

    //传入一个用于存储Api的类  这是为了方便自定义请求 比如需要手动设置Header等
    //需要手动创建一个api类 其余的步骤与原来Retrofit的方法一致
    public static <T> T selfDesign(Class<T> t) {
        T t1 = RetrofitService.getRetrofit("http://kozhnpc.work/").create(t);
        return t1;
    }

    //封装好的一个普通的get方法

    /**
     * @param url       地址
     * @param t         需要转换的bean类
     * @param netResult 返回结果回调
     * @param <T>
     */
    public static <T> void get(String url, final Class<T> t, final NetResult<T> netResult) {
        RetrofitService.getRetrofit("http://kozhnpc.work/")
                .create(MyUrl.class)
                .get(url)
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        Gson gson = new Gson();
                        String json;
                        try {
                            json = responseBody.string();
                            if (t.equals(String.class)) {
                                return (T) json;
                            } else {
                                return gson.fromJson(json, (Type) t);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(ProgressObserverUtils.applyProgressBar())
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onFailure(Throwable e, String msg_Error) {
                        netResult.onError(msg_Error);
                    }

                    @Override
                    public void onSuccess(T data) {
                        netResult.onSuccess(data);
                    }
                });
    }

    /**
     * @param url       地址
     * @param t         需要转换的bean类
     * @param netResult 返回结果回调
     * @param activity  显示loading
     * @param <T>
     */
    public static <T> void get(String url, final Class<T> t, final NetResult<T> netResult, Activity activity) {
        RetrofitService.getRetrofit("http://kozhnpc.work/")
                .create(MyUrl.class)
                .get(url)
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        Gson gson = new Gson();
                        String json;
                        try {
                            json = responseBody.string();
                            if (t.equals(String.class)) {
                                return (T) json;
                            } else {
                                return gson.fromJson(json, (Type) t);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ProgressObserverUtils.<T>applyProgressBar(activity, "请求数据中..."))
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onFailure(Throwable e, String msg_Error) {
                        netResult.onError(msg_Error);
                    }

                    @Override
                    public void onSuccess(T data) {
                        netResult.onSuccess(data);
                    }
                });
    }

    /**
     * @param url         地址
     * @param t           需要转换的bean类
     * @param netResult   返回结果回调
     * @param activity    显示loading
     * @param loadingText loading文字
     * @param <T>
     */
    public static <T> void get(String url, final Class<T> t, final NetResult<T> netResult, Activity activity, String loadingText) {
        RetrofitService.getRetrofit("http://kozhnpc.work/")
                .create(MyUrl.class)
                .get(url)
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        Gson gson = new Gson();
                        String json;
                        try {
                            json = responseBody.string();
                            if (t.equals(String.class)) {
                                return (T) json;
                            } else {
                                return gson.fromJson(json, (Type) t);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ProgressObserverUtils.<T>applyProgressBar(activity, loadingText))
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onFailure(Throwable e, String msg_Error) {
                        netResult.onError(msg_Error);
                    }

                    @Override
                    public void onSuccess(T data) {
                        netResult.onSuccess(data);
                    }
                });
    }

    //**********************************************************************//
    //封装好的一个普通的post方法

    /**
     * @param url       地址
     * @param t         需要转换的bean类
     * @param netResult 返回结果回调
     * @param <T>
     */
    public static <T> void post(String url, final Class<T> t, final NetResult<T> netResult) {
        RetrofitService.getRetrofit("http://kozhnpc.work/")
                .create(MyUrl.class)
                .post(url)
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        Gson gson = new Gson();
                        String json;
                        try {
                            json = responseBody.string();
                            if (t.equals(String.class)) {
                                return (T) json;
                            } else {
                                return gson.fromJson(json, (Type) t);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onFailure(Throwable e, String msg_Error) {
                        netResult.onError(msg_Error);
                    }

                    @Override
                    public void onSuccess(T data) {
                        netResult.onSuccess(data);
                    }
                });
    }

    /**
     * @param url       地址
     * @param t         需要转换的bean类
     * @param netResult 返回结果回调
     * @param activity  显示loading
     * @param <T>
     */
    public static <T> void post(String url, final Class<T> t, final NetResult<T> netResult, Activity activity) {
        RetrofitService.getRetrofit("http://kozhnpc.work/")
                .create(MyUrl.class)
                .post(url)
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        Gson gson = new Gson();
                        String json;
                        try {
                            json = responseBody.string();
                            if (t.equals(String.class)) {
                                return (T) json;
                            } else {
                                return gson.fromJson(json, (Type) t);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ProgressObserverUtils.<T>applyProgressBar(activity, "请求数据中..."))
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onFailure(Throwable e, String msg_Error) {
                        netResult.onError(msg_Error);
                    }

                    @Override
                    public void onSuccess(T data) {
                        netResult.onSuccess(data);
                    }
                });
    }

    /**
     * @param url         地址
     * @param t           需要转换的bean类
     * @param netResult   返回结果回调
     * @param activity    显示loading
     * @param loadingText loading文字
     * @param <T>
     */
    public static <T> void post(String url, final Class<T> t, final NetResult<T> netResult, Activity activity, String loadingText) {
        RetrofitService.getRetrofit("http://kozhnpc.work/")
                .create(MyUrl.class)
                .post(url)
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        Gson gson = new Gson();
                        String json;
                        try {
                            json = responseBody.string();
                            if (t.equals(String.class)) {
                                return (T) json;
                            } else {
                                return gson.fromJson(json, (Type) t);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ProgressObserverUtils.<T>applyProgressBar(activity, loadingText))
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onFailure(Throwable e, String msg_Error) {
                        netResult.onError(msg_Error);
                    }

                    @Override
                    public void onSuccess(T data) {
                        netResult.onSuccess(data);
                    }
                });
    }


    //******************************************************************//
    //返回结果的接口
    public interface NetResult<K> {

        void onSuccess(K k);

        void onError(String msg);

    }


}
