package com.uax.liquidflutter

import android.app.Activity
import android.content.Context
import android.os.Build
import asia.liquidinc.ekyc.applicant.LiquidSdk
import asia.liquidinc.ekyc.applicant.external.IdentifyIdChipParameters
import asia.liquidinc.ekyc.applicant.external.LiquidProcessingResultCallback
import asia.liquidinc.ekyc.applicant.external.TermsOfUseSettings
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResult
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResultStatus
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LiquidPluginModel(context: Context) {

    val sdk = LiquidSdk.getInstance(context)

    /** プラットフォームバージョン取得 */
    data class GetPlatformVersionResult(
        val platformVersion: String,
    )

    fun getPlatformVersion(): GetPlatformVersionResult {


        Timber.d("getPlatformVersion")

        try {
            return GetPlatformVersionResult(
                platformVersion = "Android ${Build.VERSION.RELEASE}"
            )
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex;
        } catch (error: Error) {
            Timber.e(error)
            throw error
        }
    }

    /** SDK バージョン取得 */
    data class GetSdkVersionResult(
        val sdkVersion: String,
    )

    fun getSdkVersion(): GetSdkVersionResult {
        Timber.d("getSdkVersion")
        return GetSdkVersionResult(
            sdkVersion = sdk.version
        )
    }

    fun startVerify(arguments: Map<*, *>) {
        val (endPointUrl, apiKey, applicant, token) = guardLet(
            arguments["endpointUrl"] as String?,
            arguments["apiKey"] as String?,
            arguments["applicant"] as String?,
            arguments["token"] as String?,
        ) { throw SdkException(SdkErrorCode.BAD_REQUEST) }

        sdk.startVerify(endPointUrl, apiKey, applicant, token)
    }

    fun showTermsOfUse(activity: Activity) {

        try {
            sdk.showTermsOfUse(TermsOfUseSettings.Builder().build(), activity)
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex;
        } catch (error: Error) {
            Timber.e(error)
            throw error
        }
    }


    data class IdentifyIdChipResult(
        val name: String,
        val birthday: String,
        val address: String,
        val sex: String,
    )

    fun identifyIdChip(activity: Activity) {

        val param = IdentifyIdChipParameters.Builder()
            .setEnabledChipForgotPin(false)
            .build()
        sdk.identifyIdChip(param, activity)

    }

    suspend fun activate() {
        val ret = suspendCoroutine { continuation ->
            val callback = LiquidProcessingResultCallback {
                continuation.resume(it)
            }
            sdk.activate(callback)
            return@suspendCoroutine
        }

        if (ret.result != LiquidProcessingResultStatus.SUCCESS) {
            throw SdkException(ret)
        }

    }

    inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
        return if (elements.all { it != null }) {
            elements.filterNotNull()
        } else {
            closure()
        }
    }

}

