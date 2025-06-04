import 'package:flutter_test/flutter_test.dart';
import 'package:liquidflutter/liquidflutter.dart';
import 'package:liquidflutter/liquidflutter_platform_interface.dart';
import 'package:liquidflutter/liquidflutter_method_channel.dart';
import 'package:liquidflutter/models/chip.dart';
import 'package:liquidflutter/models/verify_parameters.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockLiquidflutterPlatform
    with MockPlatformInterfaceMixin
    implements LiquidflutterPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String> getSdkVersion() => Future.value('test sdk version');

  @override
  Future<void> activate() async {}

  @override
  Future<Chip> identifyIdChip() {
    return Future.value(Chip(name: "", birthday: "", address: "", sex: ""));
  }

  @override
  Future<void> startVerify(VerifyParameters parameters) async {}

  @override
  Future<void> showTermsOfUse() async {
    return;
  }
}

void main() {
  final LiquidflutterPlatform initialPlatform = LiquidflutterPlatform.instance;

  test('$MethodChannelLiquidflutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelLiquidflutter>());
  });

  test('getPlatformVersion', () async {
    Liquidflutter liquidflutterPlugin = Liquidflutter();
    MockLiquidflutterPlatform fakePlatform = MockLiquidflutterPlatform();
    LiquidflutterPlatform.instance = fakePlatform;

    expect(await liquidflutterPlugin.getPlatformVersion(), '42');
  });
}
