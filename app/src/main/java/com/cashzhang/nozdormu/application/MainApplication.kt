package com.cashzhang.nozdormu.application

import android.app.Application
import com.cashzhang.nozdormu.Constants

object MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //        if (LeakCanary.isInAnalyzerProcess(this)) {
        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.

        Constants.initApplication(this)
        return
        //        }
//        LeakCanary.install(this);
        // Normal app init code...
    }
}