import Flutter
import UIKit
import Liquid

public typealias SdkCallback = (SdkData, SdkError?) -> Void

public class LiquidflutterPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "liquidflutter", binaryMessenger: registrar.messenger())
        let instance = LiquidflutterPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        debugPrint("[SDK/IOS] Call Method \(call.method)")
        let args = call.arguments as! SdkData?
        routing(call, args, {data, error in
            var res = [
                "method": call.method,
                "data": data,
            ] as SdkData

            if let error = error {
                res["error"] = error.toJson()
            }

            result(res)
        })
    }

    private func routing(_ call: FlutterMethodCall,
                         _ arguments: SdkData?,
                         _ callback: @escaping SdkCallback)  {
        switch call.method {
        case "getPlatformVersion":
            callback([
                "platformVersion": "iOS " + UIDevice.current.systemVersion,
            ], nil)

        case "getSdkVersion":
            let sex: Sex? = .female
            callback([
                "sdkVersion": LiquidEkyc.getVersion(),
                "sex": sex?.stringValue ?? ""
            ], nil)

        case "startVerify":
            guard let endPointUrl = arguments?["endpointUrl"] as? String,
                  let url = URL(string: endPointUrl),
                  let token = arguments?["token"] as? String ,
                  let applicant = arguments?["applicant"] as? String,
                  let apiKey = arguments?["apiKey"] as? String else {
                callback([:], SdkError(.badRequest, message: "invalid args"))
                return
            }

            LiquidEkyc.startVerify(
                endpoint: url,
                token: token,
                applicant: applicant,
                apiKey: apiKey
            )
            callback([:], nil)

        case "showTermsOfUse":
            let argVc = arguments?["viewController"] as? UIViewController
            guard let vc = argVc ?? topViewController() else {
                callback([:], SdkError(.badRequest))
                return
            }
            LiquidEkyc.showTermsOfUse(on: vc) { procResult in
                if procResult.status == .success {
                    callback([:], nil)
                } else {
                    callback([:], SdkError(procResult))
                }
            }

        case "identifyIdChip":
            let argVc = arguments?["viewController"] as? UIViewController
            guard let vc = argVc ?? topViewController() else {
                callback([:], SdkError(.badRequest))
                return
            }
            let parameters = IdentifyIdChipParametersBuilder()
            // .setBase64TargetData("電子署名生成処理で使用する電子署名対象データをBASE64エンコードした値")
                .setEnabledChipForgotPin(false)
                .build()
            LiquidEkyc.identifyIdChip(parameters, on: vc) { idChipResult in
                guard idChipResult.result.status == .success,
                      let data = idChipResult.chipData else {
                    callback([:], SdkError(idChipResult.result))
                    return
                }

                callback([
                    "name": data.name,
                    "birthday": data.birthday,
                    "address": data.address,
                    "sex": data.sex?.stringValue ?? ""
                ], nil)
            }

        case "activate":
            LiquidEkyc.activate { procResult in
                if procResult.status == .success {
                    callback([:], nil)
                } else {
                    callback([:], SdkError(procResult))
                }
            }

        default:
            callback([:], SdkError(.invalidMethodName))
        }
    }
}

private func topViewController() -> UIViewController? {
    var vc = UIApplication.shared.keyWindow?.rootViewController
    while vc?.presentedViewController != nil {
        vc = vc?.presentedViewController
    }
    return vc
}

