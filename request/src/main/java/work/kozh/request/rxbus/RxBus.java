package work.kozh.request.rxbus;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private volatile static RxBus mDefaultInstance;
    private final Subject<Object> mBus;

    private HashMap<String, CompositeDisposable> mSubscriptionMap;

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    public static RxBus get() {
        return Holder.BUS;
    }

    public void post(Object obj) {
        mBus.onNext(obj);
    }

    private <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }


    /**
     * 保存订阅后的disposable
     *
     * @param o
     * @param disposable
     */
    private void addSubscription(Object o, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = o.getClass().getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
//            LogUtils.i(key + " 中RxBus存储的对象数量：" + disposables.size());
            mSubscriptionMap.put(key, disposables);
        }
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    private void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }

        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
//            LogUtils.i("移除" + key + " 中RxBus存储的对象");
            int size = mSubscriptionMap.get(key).size();
//            LogUtils.i(key + "中之前RxBus存储的对象数量：" + size);
            mSubscriptionMap.get(key).dispose();
        }

        mSubscriptionMap.remove(key);
    }


    //订阅事件结果监听方法

    //外部只需要接收这一个方法即可  发送使用post()  接收使用这个方法  RxBus的基本使用完毕

    /**
     * @param object     页面对象
     * @param eventClass 事件class
     * @param listener   结果回调
     * @param <T>
     */
    public <T> void receiveEventResult(Object object, Class<T> eventClass, final SubscribeRxBusResult<T> listener) {
        Disposable subscribe = RxBus.getInstance()
                .toObservable(eventClass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        listener.onResult(t);
                    }
                });
        addSubscription(object, subscribe);
    }

    public interface SubscribeRxBusResult<K> {
        void onResult(K k);
    }


    /**
     *
     * 使用方法
     *
     * 比如A跳转到B ，我这边是recycleview 的item点击跳转，传一个对象
     *
     *   @Override
     *         public void onItemClick(View view, int position) {
     *             ActicleBean.OthersBean bean = mDataList.get(position);
     *             RxBus.getDefault().post(bean);
     *             startActivity(new Intent(ArticleActivity.this,ActicleListActivity.class));
     *         }
     *
     * 在B类上 直接写就可以获得了你所需要传的对象
     *
     *  @Override
     *     protected void onCreate(Bundle savedInstanceState) {
     *         super.onCreate(savedInstanceState);
     *         setContentView(R.layout.activity_acticle_list);
     *         operateBus();
     *      }
     *
     *  RxBus
     *  private void operateBus() {
     *         RxBus.getDefault().toObservable()
     *                 .map(new Function<Object, ActicleBean.OthersBean>() {
     *                     @Override
     *                     public ActicleBean.OthersBean apply(@NonNull Object o) throws Exception {
     *                         return (ActicleBean.OthersBean) o;
     *                     }
     *
     *                 })
     *                 .subscribe(new Consumer<ActicleBean.OthersBean>() {
     *                     @Override
     *                     public void accept(@NonNull ActicleBean.OthersBean othersBean) throws Exception {
     *                         if (othersBean != null) {
     *                             ToastUtils.toast(ActicleListActivity.this, "othersBean" + othersBean.getDescription());
     *                         }
     *                     }
     *
     *                 });
     *     }
     *
     *
     */


}
