package com.cashzhang.nozdormu;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {
    public static void CustomSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(observer);
    }
}
