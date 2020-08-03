package com.cashzhang.nozdormu

import java.io.IOException

interface CustomListener<T> {
    @Throws(IOException::class, ClassNotFoundException::class)
    fun onNext(t: T)
    fun onComplete()
}