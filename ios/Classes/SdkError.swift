import Foundation
import Liquid

public enum SdkErrorCode: String {
    case invalidMethodName
    case badRequest
    case liquid

    case unknown
    func toString() -> String { rawValue }
}

public class SdkError: Error {
    public let code: SdkErrorCode
    public let message: String?
    public let details: Any?

    init(_ code: SdkErrorCode, message: String? = nil, details: Any? = nil) {
        self.code = code
        self.message = message
        self.details = details
    }

    init(_ result: ProcResult) {
        self.code = .liquid
        self.message = nil
        self.details = result.toJson()
    }

    func toJson() -> SdkData {
        return [
            "code": code.toString(),
            "message": message,
            "details": details,
        ]
    }
}
