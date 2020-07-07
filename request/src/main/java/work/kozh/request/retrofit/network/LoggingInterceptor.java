package work.kozh.request.retrofit.network;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        /*//移除旧的 header 便于适配电脑端
        request.newBuilder().removeHeader("User-Agent").addHeader("User-Agent", WebSettings.getDefaultUserAgent(NetUIUtils.getContext())).build();
*/

        Response response = chain.proceed(request);
        long t1 = System.nanoTime();//请求发起的时间

        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
               /* NetLogUtils.i(String.format("发送请求 %s on %s %n%s %nRequestParams:{%s}",
                        request.url(), chain.connection(), request.headers(), sb.toString()));*/
            }
        } else {
            if (request.url().toString().contains("wufazhuce.com/one")) {
                //这里不能直接使用response.body().string()的方式输出日志
                //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
                //个新的response给应用层处理
                /*ResponseBody responseBody = response.body();
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                //获得返回的数据  41341d356f9bbb74a0ffd39c1492096ca806b475  41341d356f9bbb74a0ffd39c1492096ca806b475
                Buffer buffer = source.buffer();
                String html = buffer.clone().readString(Charset.forName("UTF-8"));
                String token = html.split("One.token = '")[1].split("'")[0];
                if (token != null) {
                    SpUtils.putString(NetUIUtils.getContext(), CONSTANT_ONE_TOKEN, token);
                    NetLogUtils.i("获取的token：" + token);
                }*/

            }
            String cookie = request.headers().toString();
//            NetLogUtils.i("获取的cookie：" + cookie);

           /* NetLogUtils.i(String.format("发送请求 %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));*/

            /*ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            //获得返回的数据  41341d356f9bbb74a0ffd39c1492096ca806b475  41341d356f9bbb74a0ffd39c1492096ca806b475
            Buffer buffer = source.buffer();
            String html = buffer.clone().readString(Charset.forName("UTF-8"));
            String token = html.split("One.token = '")[1].split("'")[0];
            if (token != null) {
                SpUtils.putString(NetUIUtils.getContext(), CONSTANT_ONE_TOKEN, token);
                NetLogUtils.i("获取的token2：" + token);
            }*/

        }
        return response;
    }


}
