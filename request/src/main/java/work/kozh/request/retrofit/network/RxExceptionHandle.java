package work.kozh.request.retrofit.network;

import android.net.ParseException;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


//该类属于对请求数据失败后的处理方式
//在successObserver 的OnError()中调用


public class RxExceptionHandle {
    public static String exceptionHandler(Throwable e) {

        String errorMsg = "未知错误";
        if (e instanceof UnknownHostException) {
            errorMsg = "网络不可用";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "请求网络超时";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException
                || e instanceof JSONException) {
            errorMsg = "数据解析错误";
        } else {
            errorMsg = "其他类型错误：" + e.getMessage();
        }
        return errorMsg;
    }

    private static String convertStatusCode(HttpException httpException) {

        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "服务器处理请求出错";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "服务器无法处理请求";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

}
