package work.kozh.request.retrofit.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    public Context mContext;
    /*
    该类用于处理缓存
     */

    public CacheInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();//拦截Request对象
        Log.d("OkHttpUtils", "request.url():" + request.url());
        Response response = chain.proceed(request);//获取服务器响应
        if (TextUtils.isEmpty(response.cacheControl().toString())) {

            int time = 0;
            //判断是否有网络 如果有网络就加载网络数据 没有网络就加载缓存数据，2周
            if (isNetworkAvailable(mContext)) {
                time = 60;
            } else {
                time = 60 * 60 * 24 * 14;
            }

            //生成新的response对象 往新的对象中手动添加Cache-Control 让其支持缓存
            Response newReponse = response.newBuilder().addHeader("Cache-Control", "max-age=" + time).build();
            return newReponse;
        }

        return response;
    }

    //判断是否有网络
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        boolean available = info.isAvailable();
        return available;
    }
}
