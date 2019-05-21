package com.cashzhang.nozdormu;

import android.util.Log;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CustomObserver<T> implements Observer<T> {
    private static final String TAG = CustomObserver.class.getSimpleName();
    private CustomListener<T> listener;
    public CustomObserver (CustomListener<T> listener){
        this.listener = listener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.d(TAG, "onSubscribe");
    }

    @Override
    public void onNext(T t) {
        try {
            listener.onNext(t);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError: "+e.getMessage());
    }

    @Override
    public void onComplete() {
        listener.onComplete();
//        Log.d(TAG, "onComplete");
    }
}
