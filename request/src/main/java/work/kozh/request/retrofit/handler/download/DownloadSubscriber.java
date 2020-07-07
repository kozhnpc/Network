package work.kozh.request.retrofit.handler.download;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DownloadSubscriber implements Observer {

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
        onDownloadSuccess();
    }

    public abstract void onDownloadSuccess();


}
