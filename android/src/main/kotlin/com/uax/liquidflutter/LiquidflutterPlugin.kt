package com.uax.liquidflutter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import asia.liquidinc.ekyc.applicant.external.result.LiquidChipIdentificationResult
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResult
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResultStatus
import asia.liquidinc.ekyc.applicant.external.result.chip.ChipIdentificationResultStatus
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/** LiquidflutterPlugin */
class LiquidflutterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    PluginRegistry.ActivityResultListener {

    private var _sdkModel: LiquidPluginModel? = null
    private val sdkModel get() = requireNotNull(_sdkModel) { "sdkModel is not attached" }

    private lateinit var activity: Activity
    private lateinit var context: Context

    //onActivityResultの結果待ち合わせ用
    private var procResult = CompletableDeferred<ProcessResult>()
    private var idChipResult = CompletableDeferred<IdentifyChipResult>()

    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "liquidflutter")
        channel.setMethodCallHandler(this)

        context = flutterPluginBinding.applicationContext
        _sdkModel = LiquidPluginModel(context)
    }

    override fun onMethodCall(call: MethodCall, @NonNull result: Result) {
        if (procResult.isActive) procResult.cancel()
        procResult = CompletableDeferred()
        if (idChipResult.isActive) idChipResult.cancel()
        idChipResult = CompletableDeferred()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                result.success(handle(call))
            } catch (e: NotImplementedError) {
                result.notImplemented()
            } catch (e: Throwable) {
                result.error(e.javaClass.simpleName, e.message, null)
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)

        _sdkModel = null
    }

    private suspend fun handle(call: MethodCall): Any {
        val data = try {
            routing(call)
        } catch (e: SdkException) {
            return hashMapOf(
                "error" to hashMapOf(
                    "code" to e.code.codeName,
                    "message" to e.message,
                    "details" to e.details.asEncodable(),
                )
            )
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex;
        } catch (error: Error) {
            Timber.e(error)
            throw error
        }
        return sdkResultOf(data.asEncodable())
    }

    private fun sdkResultOf(data: Any?) = hashMapOf("data" to data)

    private suspend fun routing(call: MethodCall): Any {
        return when (call.method) {
            "getPlatformVersion" -> sdkModel.getPlatformVersion()
            "getSdkVersion" -> sdkModel.getSdkVersion()
            "startVerify" -> sdkModel.startVerify(call.arguments as Map<*, *>)
            "showTermsOfUse" -> showTermsOfUse()
            "identifyIdChip" -> identifyIdChip()
            "activate" -> sdkModel.activate()
            else -> throw SdkException(SdkErrorCode.INVALID_METHOD_NAME)
        }
    }

    private suspend fun showTermsOfUse(): Any {
        sdkModel.showTermsOfUse(activity)
        val ret = procResult.await()

        if (ret.result.result != LiquidProcessingResultStatus.SUCCESS) {
            throw SdkException(ret.result)
        }
        return ""
    }

    private suspend fun identifyIdChip(): Any {
        sdkModel.identifyIdChip(activity)
        val ret = idChipResult.await()

        if (ret.result.resultStatus != ChipIdentificationResultStatus.SUCCESS) {
            throw SdkException(ret.result)
        }

        ret.result.liquidChipData?.let {
            return LiquidPluginModel.IdentifyIdChipResult(
                it.name,
                it.birthday,
                it.address,
                it.sex.stringValue()
            )
        }

        throw SdkException(SdkErrorCode.UNKNOWN)
    }


    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
//        Timber.tag("uma").d("onActivityResult")
        Log.d("uma", "onActivityResult")

        sdkModel.sdk.handleShowTermsOfUseResult(requestCode, resultCode, data) { result ->
//            Timber.tag("uma").d(result.result.name)
            Log.d("uma", result.result.name)

            GlobalScope.launch(Dispatchers.Main) {
                procResult.complete(ProcessResult(result))
            }
        }

        sdkModel.sdk.handleChipIdentificationResult(requestCode, resultCode, data) { result ->
//            Timber.tag("uma").d(result.result.name)

            GlobalScope.launch(Dispatchers.Main) {
                idChipResult.complete(IdentifyChipResult(result))
            }
        }


        return false
    }
}

class ProcessResult(val result: LiquidProcessingResult) {}
class IdentifyChipResult(val result: LiquidChipIdentificationResult) {}
