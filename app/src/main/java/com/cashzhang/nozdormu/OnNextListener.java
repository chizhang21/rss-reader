package com.cashzhang.nozdormu;

import java.io.IOException;

public interface OnNextListener<T> {
    void onNext(T t) throws IOException, ClassNotFoundException;
}
