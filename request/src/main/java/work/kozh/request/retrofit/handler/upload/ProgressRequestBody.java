package work.kozh.request.retrofit.handler.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {

    private RequestBody requestBody;

    private ProgressUploadListener mListener;

    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, ProgressUploadListener listener) {
        this.requestBody = requestBody;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                mListener.onUploadProgress(bytesWritten, contentLength);
            }
        };
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }
}
