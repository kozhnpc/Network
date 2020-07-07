package work.kozh.request.retrofit;

import android.app.Application;
import android.content.Context;


/**
 * Created by 00115702 on 2018/12/12.
 */

public class NetApplication extends Application {

    public static Context ctx;


    private static NetApplication sInstance;


    /**
     * 获取Application对象
     *
     * @return
     */
    public static synchronized NetApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        //获取全局变量   用这个 context可以避免内存泄漏  尽量使用
        ctx = getApplicationContext();

    }

    public static Context getCtx() {
        return ctx;
    }


}


