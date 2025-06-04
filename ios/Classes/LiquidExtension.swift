import Foundation
import Liquid

extension ProcResult {
    func toJson() -> SdkData {
        return [
            "status": status.stringValue,
            "resultCode": resultCode,
            "additionalData" : [
                "title": additionalData?.maintenanceTitle,
                "message": additionalData?.maintenanceMessage,
            ],
        ]
    }
}

extension Liquid.Sex {
    var stringValue: String {
        switch self {
        case .male:
            return "0"
        case .female:
            return "1"
        case .others:
            return  "2"
        @unknown default:
            return "unknown"
        }
    }
}

extension Liquid.ResultStatus {
    var stringValue: String {
        switch self {
        case .success:
            return "success"
        case .maintenance:
            return "maintenance"
        case .permissionNotAllowed:
            return "permissionNotAllowed"
        case .screenTimeout:
            return "screenTimeout"
        case .sessionTimeout:
            return "sessionTimeout"
        case .ocrInProgress:
            return "ocrInProgress"
        case .ocrUnSupported:
            return "ocrUnSupported"
        case .userCancel:
            return "userCancel"
        case .error:
            return "error"
        case .chipLocked:
            return "chipLocked"
        case .chipUnusual:
            return "chipUnusual"
        case .chipUnusualUpdated:
            return "chipUnusualUpdated"
        case .chipVerifyFailure:
            return "chipVerifyFailure"
        case .chipUnusualResidencecard:
            return "chipUnusualResidencecard"
        case .chipMissingExternalChar:
            return "chipMissingExternalChar"
        case .unsupportedChip:
            return "unsupportedChip"
        case .chipPinFailure:
            return "chipPinFailure"
        case .chipExpired:
            return "chipExpired"
        case .chipForgotPin:
            return "chipForgotPin"
        case .chipIdentifyDenied:
            return "chipIdentifyDenied"
        case .chipIdentifyError:
            return "chipIdentifyError"
        case .termsDoNotAgree:
            return "termsDoNotAgree"
        case .communicationFailure:
            return "communicationFailure"
        @unknown default:
            return "unknown"
        }
    }
}
