package com.enbcreative.androidbackgroundservices.utils

import android.util.Log
import com.enbcreative.androidbackgroundservices.BuildConfig

fun Any.logd(message: String) {
    if (BuildConfig.DEBUG) Log.d(this.javaClass.simpleName, message)
}