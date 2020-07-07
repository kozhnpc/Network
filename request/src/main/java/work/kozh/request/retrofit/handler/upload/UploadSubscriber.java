package work.kozh.request.retrofit.handler.upload;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class UploadSubscriber implements Observer {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        onUploadSuccess();
    }

    public abstract void onUploadSuccess();


}
