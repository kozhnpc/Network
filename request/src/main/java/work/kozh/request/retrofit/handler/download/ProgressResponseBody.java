package work.kozh.request.retrofit.handler.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

    private ProgressDownloadListener listener;
    private ResponseBody responseBody;
    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ProgressDownloadListener listener, ResponseBody responseBody) {
        this.listener = listener;
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(BufferedSource source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount); // read() returns the number of
                // bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != listener) {
                    if (bytesRead != -1) {
//                        NetLogUtils.i("当前下载进度为：" + (int) (totalBytesRead * 100 / responseBody.contentLength()) + "%");
//                        listener.onDownloadProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                        listener.onDownloadProgress(totalBytesRead, responseBody.contentLength());
                    }
                }
                return bytesRead;
            }
        };
    }
}
