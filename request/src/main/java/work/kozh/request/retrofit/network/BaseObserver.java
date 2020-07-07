package work.kozh.request.retrofit.network;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    //该类属于对请求数据成功后的处理方式

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        //在这里对数据进行判断，如果有后台的话
        /*if (tHttpResult.getCode() == 200) {
            onSuccess(tHttpResult.getInitData());
        } else {
            onFailure(null, tHttpResult.getMessage());
        }*/
        //或者进行其他的操作

        onSuccess(t);


    }


    public abstract void onFailure(Throwable e, String msg_Error);

    public abstract void onSuccess(T data);

    @Override
    public void onError(Throwable e) {
//        NetLogUtils.i("BaseObservable捕获到的错误信息：" + e.getMessage());
        onFailure(e, RxExceptionHandle.exceptionHandler(e));
    }

    @Override
    public void onComplete() {

    }
}
