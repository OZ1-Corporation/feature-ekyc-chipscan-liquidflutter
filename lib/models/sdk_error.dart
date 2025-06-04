import 'raw_json.dart';

class SdkError implements Exception {
  final RawJson raw;
  final SdkErrorCode code;
  final String message;
  final RawJson? detail;

  SdkError({
    required this.raw,
    this.message = "Unknown Error",
    this.code = SdkErrorCode.unknown,
    this.detail,
  });

  static SdkError? fromRawResponse(RawJson raw) {
    return fromRawError(raw);
  }

  static SdkError? fromRawError(RawJson raw) {
    if (raw["error"] == null) {
      return null;
    }

    if (raw["error"]["code"] == "liquid") {
      return fromLiquidError(raw);
    }

    return SdkError(
      raw: raw["error"],
      code: _codeFrom(raw["error"]["code"]),
      message: raw["error"]["message"] ?? "Unknown Error",
      detail: raw["error"]["detail"],
    );
  }

  static SdkError? fromLiquidError(RawJson raw) {
    final liquidError = raw["error"]["details"];

    return SdkError(
      raw: liquidError,
      code: _codeFrom(liquidError["status"]),
      message: liquidError["additionalData"]["message"] ?? "Liquid Error",
      detail: liquidError["resultCode"],
    );
  }

  @override
  String toString() {
    return "SdkError code \"${code.name}\" / $message";
  }

  static SdkErrorCode _codeFrom(String code) {
    return SdkErrorCode.values.firstWhere(
      (element) => element.name == code,
      orElse: () => SdkErrorCode.unknown,
    );
  }
}

enum SdkErrorCode {
  // normal
  invalidMethodName,
  badRequest,

  // liquid
  maintenance,
  permissionNotAllowed,
  screenTimeout,
  sessionTimeout,
  ocrInProgress,
  ocrUnSupported,
  userCancel,
  error,
  chipLocked,
  chipUnusual,
  chipUnusualUpdated,
  chipVerifyFailure,
  chipUnusualResidencecard,
  chipMissingExternalChar,
  unsupportedChip,
  chipPinFailure,
  chipExpired,
  chipForgotPin,
  chipIdentifyDenied,
  chipIdentifyError,
  termsDoNotAgree,
  communicationFailure,

  // unknown
  unknown;

  static SdkErrorCode codeFrom(int code) {
    return SdkErrorCode.values.firstWhere(
      (element) => element.name == code,
      orElse: () => SdkErrorCode.unknown,
    );
  }
}
