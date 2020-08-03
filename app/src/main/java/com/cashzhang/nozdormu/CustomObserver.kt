package com.cashzhang.nozdormu

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.IOException

class CustomObserver<T>(private val listener: CustomListener<T>) : Observer<T> {
    override fun onSubscribe(d: Disposable) {
        Log.d(TAG, "onSubscribe")
    }

    override fun onNext(t: T) {
        try {
            listener.onNext(t)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onError(e: Throwable) {
        Log.d(TAG, "onError: " + e.message)
    }

    override fun onComplete() {
        listener.onComplete()
        //        Log.d(TAG, "onComplete");
    }

    companion object {
        private val TAG = CustomObserver::class.java.simpleName
    }

}