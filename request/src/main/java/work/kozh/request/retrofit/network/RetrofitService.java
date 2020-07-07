package work.kozh.request.retrofit.network;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import work.kozh.request.retrofit.utils.NetLogUtils;
import work.kozh.request.retrofit.utils.NetUIUtils;

public class RetrofitService {

    //    private static MyUrl apiUrl;
    private static Retrofit retrofit;

    //实际应用时需要传入不同的API类，并create()获得相应的API对象，以满足不同的API类的需求
    //单例模式
    public static Retrofit getRetrofit(String baseUrl) {
        if (retrofit == null) {
            synchronized (RetrofitService.class) {
                if (retrofit == null) {
//                    retrofit = new RetrofitService().getRetrofit();
                    retrofit = initRetrofit(initOkHttp(), baseUrl);
                }
            }
        }
        return retrofit;
    }


    //---------- 可以复用的方法 ------------

    private static OkHttpClient initOkHttp() {
        Cache cache = new Cache(NetUIUtils.getContext().getCacheDir(), 1024 * 1024 * 10);
        //加入cookie持久化
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(NetUIUtils.getContext()));
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                NetLogUtils.i(message);
            }
        });
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient().newBuilder()
                .cache(cache)
                .addInterceptor(new CacheInterceptor(NetUIUtils.getContext()))
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cookieJar(cookieJar)
//                .addInterceptor(logging)
//                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    private static Retrofit initRetrofit(OkHttpClient client, String baseUrl) {

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
