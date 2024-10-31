package com.basicbiometrics

import android.app.Activity
import android.content.pm.PackageManager
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.UiThreadUtil

@ReactModule(name = BasicBiometricsModule.NAME)
class BasicBiometricsModule(reactContext: ReactApplicationContext) :
    BasicBiometricsSpec(reactContext) {

    companion object {
        const val NAME = "BasicBiometrics"
        private const val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
    }

    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    override fun canAuthenticate(promise: Promise) {
        try {
            val biometricManager = BiometricManager.from(reactApplicationContext)
            val res = biometricManager.canAuthenticate(authenticators)
            val canAuthenticate = res == BiometricManager.BIOMETRIC_SUCCESS
            promise.resolve(canAuthenticate)
        } catch (e: Exception) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun requestBioAuth(title: String, subtitle: String, promise: Promise) {
        UiThreadUtil.runOnUiThread {
            try {
                val context = reactApplicationContext
                val activity = currentActivity
                val executor = ContextCompat.getMainExecutor(context)

                val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        promise.reject(Exception(errString.toString()))
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        promise.resolve(true)
                    }
                }

                if (activity != null) {
                    val prompt = BiometricPrompt(activity as FragmentActivity, executor, authenticationCallback)
                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setAllowedAuthenticators(authenticators)
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .build()
                    prompt.authenticate(promptInfo)
                } else {
                    throw Exception("null activity")
                }
            } catch (e: Exception) {
                promise.reject(e)
            }
        }
    }

    @ReactMethod
    fun getBiometryType(promise: Promise) {
        try {
            val pm = reactApplicationContext.packageManager
            val biometryType = when {
                pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) -> "Fingerprint"
                pm.hasSystemFeature(PackageManager.FEATURE_FACE) -> "Face"
                else -> "None"
            }
            promise.resolve(biometryType)
        } catch (e: Exception) {
            promise.resolve("Unknown")
        }
    }
}
