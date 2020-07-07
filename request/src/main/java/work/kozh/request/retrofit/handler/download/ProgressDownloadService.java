package work.kozh.request.retrofit.handler.download;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ProgressDownloadService {

    @Streaming
    @Headers("Accept-Encoding:identity")
    @GET()
    Observable<ResponseBody> download(@Url String url);

}
