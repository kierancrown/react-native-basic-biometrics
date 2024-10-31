package com.basicbiometrics

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise

abstract class BasicBiometricsSpec internal constructor(context: ReactApplicationContext) :
    ReactContextBaseJavaModule(context) {

    abstract fun canAuthenticate(promise: Promise)
    abstract fun requestBioAuth(title: String, subtitle: String, promise: Promise)
    abstract fun getBiometryType(promise: Promise)
}
