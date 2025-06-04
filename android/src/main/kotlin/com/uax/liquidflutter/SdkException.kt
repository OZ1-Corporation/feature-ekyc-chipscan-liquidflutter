package com.uax.liquidflutter

import asia.liquidinc.ekyc.applicant.external.result.LiquidChipIdentificationResult
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResult

class SdkException(
    val code: SdkErrorCode,
    message: String? = null,
    val details: Any? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(result: LiquidProcessingResult) : this(
        SdkErrorCode.LIQUID,
        null,
        result.toJson()
        )

    constructor(result: LiquidChipIdentificationResult) : this(
        SdkErrorCode.LIQUID,
        null,
        result.toJson()
    )
}

enum class SdkErrorCode(
    val codeName: String,
) {
    LIQUID("liquid"),
    INVALID_METHOD_NAME("invalidMethodName"),
    BAD_REQUEST("badRequest"),
    UNKNOWN("unknown"),
}
