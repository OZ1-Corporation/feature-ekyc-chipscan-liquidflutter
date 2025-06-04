import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'liquidflutter_platform_interface.dart';
import 'models/chip.dart';
import 'models/verify_parameters.dart';
import 'models/raw_json.dart';
import 'models/sdk_error.dart';

/// An implementation of [LiquidflutterPlatform] that uses method channels.
class MethodChannelLiquidflutter extends LiquidflutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('liquidflutter');

  Future<RawJson> invoke(String method, [dynamic arguments]) async {
    final result = await methodChannel.invokeMethod(method, arguments);
    final error = SdkError.fromRawResponse(result);
    if (error != null) {
      throw error;
    }
    return result["data"] ?? {};
  }

  @override
  Future<String?> getPlatformVersion() async {
    final RawJson json = await await invoke('getPlatformVersion');
    return json["platformVersion"];
  }

  @override
  Future<String> getSdkVersion() async {
    final RawJson json = await invoke('getSdkVersion');
    return json["sdkVersion"] ?? "null";
  }

  @override
  Future<void> startVerify(VerifyParameters parameters) async {
    return invoke('startVerify', parameters.toParam());
  }

  @override
  Future<Chip> identifyIdChip() async {
    final RawJson json = await invoke('identifyIdChip');
    return Chip.fromJson(json);
  }

  @override
  Future<void> activate() async {
    return invoke('activate');
  }

  @override
  Future<void> showTermsOfUse() async {
    await invoke('showTermsOfUse');
  }
}
