package work.kozh.request.retrofit.handler.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

//拦截器，用于修改 BodyResponse

public class ProgressDownloadInterceptor implements Interceptor {
    private ProgressDownloadListener listener;

    public ProgressDownloadInterceptor(ProgressDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new ProgressResponseBody(listener, response.body())).build();
    }


}
