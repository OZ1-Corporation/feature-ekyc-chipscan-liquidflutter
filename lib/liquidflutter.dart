import 'liquidflutter_platform_interface.dart';
import 'models/chip.dart';
import 'models/verify_parameters.dart';

class Liquidflutter {
  final LiquidflutterPlatform _platform;

  Liquidflutter([LiquidflutterPlatform? platform])
      : _platform = platform ?? LiquidflutterPlatform.instance;

  Future<String?> getPlatformVersion() {
    return _platform.getPlatformVersion();
  }

  Future<String> getSdkVersion() {
    return _platform.getSdkVersion();
  }

  Future<void> startVerify(VerifyParameters parameters) {
    return _platform.startVerify(parameters);
  }

  Future<void> showTermsOfUse() {
    return _platform.showTermsOfUse();
  }

  Future<Chip> identifyIdChip() {
    return _platform.identifyIdChip();
  }

  Future<void> activate() {
    return _platform.activate();
  }
}
