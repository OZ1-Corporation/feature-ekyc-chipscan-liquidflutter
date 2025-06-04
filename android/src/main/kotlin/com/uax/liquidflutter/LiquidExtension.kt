package com.uax.liquidflutter

import asia.liquidinc.ekyc.applicant.external.result.LiquidChipIdentificationResult
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResult
import asia.liquidinc.ekyc.applicant.external.result.LiquidProcessingResultStatus
import asia.liquidinc.ekyc.applicant.external.result.chip.ChipIdentificationResultStatus
import asia.liquidinc.ekyc.applicant.external.result.chip.Sex

fun Sex.stringValue(): String {
    return when (this) {
        Sex.MALE -> "0"
        Sex.FEMALE -> "1"
        Sex.OTHERS -> "2"
        else -> "unknown"
    }
}

fun LiquidProcessingResult.toJson(): Any {
    return hashMapOf(
        "status" to result.stringValue(),
        "resultCode" to errorCode,
        "additionalData" to hashMapOf(
            "title" to additionalDataTitle,
            "message" to additionalDataMessage
        )
    )
}

fun LiquidProcessingResultStatus.stringValue(): String {
    return when (this) {
        LiquidProcessingResultStatus.USER_CANCEL -> "userCancel"
        LiquidProcessingResultStatus.MAINTENANCE -> "maintenance"
        LiquidProcessingResultStatus.ERROR -> "error"
        LiquidProcessingResultStatus.SESSION_TIMEOUT -> "sessionTimeout"
        LiquidProcessingResultStatus.TERMS_DO_NOT_AGREE -> "termsDoNotAgree"
        LiquidProcessingResultStatus.COMMUNICATION_FAILURE -> "communicationFailure"
        LiquidProcessingResultStatus.SUCCESS -> "success"
        else -> "unknown"
    }
}

fun LiquidChipIdentificationResult.toJson(): Any {
    return hashMapOf(
        "status" to resultStatus.stringValue(),
        "resultCode" to errorCode,
        "additionalData" to hashMapOf(
            "title" to additionalDataTitle,
            "message" to additionalDataMessage
        )
    )
}

fun ChipIdentificationResultStatus.stringValue(): String {
    return when (this) {
        ChipIdentificationResultStatus.USER_CANCEL -> "userCancel"
        ChipIdentificationResultStatus.MAINTENANCE -> "maintenance"
        ChipIdentificationResultStatus.ERROR -> "error"
        ChipIdentificationResultStatus.SESSION_TIMEOUT -> "sessionTimeout"
        ChipIdentificationResultStatus.SCREEN_TIMEOUT -> "screenTimeout"
        ChipIdentificationResultStatus.SUCCESS -> "success"
        ChipIdentificationResultStatus.PERMISSION_NOT_ALLOWED -> "permissionNotAllowed"
        ChipIdentificationResultStatus.UNSUPPORTED_DEVICE_NFC -> "unsupportedChip"
        ChipIdentificationResultStatus.IC_CHIP_FORGOT_PIN -> "chipForgotPin"
        ChipIdentificationResultStatus.IC_CHIP_LOCKED -> "chipLocked"
        ChipIdentificationResultStatus.IC_CHIP_EXPIRED -> "chipExpired"
        ChipIdentificationResultStatus.IC_CHIP_IDENTIFY_DENIED -> "chipIdentifyDenied"
        ChipIdentificationResultStatus.IC_CHIP_IDENTIFY_ERROR -> "chipIdentifyError"
        else -> "unknown"
    }
}


