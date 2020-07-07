package work.kozh.request.retrofit.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface MyUrl {

    //*********************************************************************************//

    //提取出通用的请求框架  GET
    @GET()
    Observable<ResponseBody> get(@Url String url);

    //提取出通用的请求框架  POST
    @POST()
    Observable<ResponseBody> post(@Url String url);

    //*********************************************************************************//

}
