import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'liquidflutter_method_channel.dart';
import 'models/chip.dart';
import 'models/verify_parameters.dart';

abstract class LiquidflutterPlatform extends PlatformInterface {
  /// Constructs a LiquidflutterPlatform.
  LiquidflutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static LiquidflutterPlatform _instance = MethodChannelLiquidflutter();

  /// The default instance of [LiquidflutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelLiquidflutter].
  static LiquidflutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [LiquidflutterPlatform] when
  /// they register themselves.
  static set instance(LiquidflutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String> getSdkVersion();

  Future<void> startVerify(VerifyParameters parameters);

  Future<void> showTermsOfUse();

  Future<Chip> identifyIdChip();

  Future<void> activate();
}
