package work.kozh.request.retrofit.handler.upload;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ProgressUploadService {
    @Multipart
    @POST()
    Observable<RequestBody> upload(MultipartBody.Part file);

}
