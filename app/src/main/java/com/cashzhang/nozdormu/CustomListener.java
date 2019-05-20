package com.cashzhang.nozdormu;

import java.io.IOException;

public interface CustomListener<T> {
    void onNext(T t) throws IOException, ClassNotFoundException;
    void onComplete();
}
