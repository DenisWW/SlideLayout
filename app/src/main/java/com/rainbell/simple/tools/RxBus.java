package com.rainbell.simple.tools;


import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by wanxiu on 2016/6/23.
 */
public class RxBus {

    private HashMap<String, CompositeDisposable> mSubscriptionMap;
    private static volatile RxBus mRxBus1;
    private final Subject<Object> mSubject;

    //单列模式
//    public static RxBus getInstance() {
//        if (mRxBus1 == null) {
//            synchronized (RxBus.class) {
//                if (mRxBus1 == null) {
//                    mRxBus1 = new RxBus();
//                }
//            }
//        }
//        return mRxBus1;
//    }
    //单列模式
    public static RxBus getInstance() {
        return RxBusIntances.INTANCES.getRxBusIntances();
    }

    private enum RxBusIntances {
        INTANCES;
        private RxBus mRxBus;

        RxBusIntances() {
            mRxBus = new RxBus();
        }

        private RxBus getRxBusIntances() {
            return mRxBus;
        }

    }


    public RxBus() {
        mSubject = PublishSubject.create().toSerialized();
    }

    public void post(Object o) {
        mSubject.onNext(o);
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param <T>
     * @param type
     * @return
     */
    public <T> Flowable<T> getObservable(Class<T> type) {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER)
                .ofType(type);
    }

    /**
     * 一个默认的订阅方法
     * 使用RxLifecycle
     *
     * @param <T>
     * @param type
     * @param next
     * @param error
     * @return
     */
    public <T> Disposable doSubscribe(LifecycleTransformer transformer, Class<T> type, Consumer<T> next, Consumer<Throwable> error) {
        return getObservable(type)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);
    }

    /**
     * 一个默认的订阅方法
     *
     * @param <T>
     * @param type
     * @param next
     * @param error
     * @return
     */
    public <T> Disposable doSubscribe(Class<T> type, Consumer<T> next, Consumer<Throwable> error) {
        return getObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);
    }

    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 保存订阅后的disposable
     *
     * @param o
     * @param disposable
     */
    public void addSubscription(Object o, Disposable disposable) {
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
            mSubscriptionMap.put(key, disposables);
        }
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    public void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }

        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }

        mSubscriptionMap.remove(key);
    }
}
