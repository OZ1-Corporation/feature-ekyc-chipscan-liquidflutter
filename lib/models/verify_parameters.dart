import 'raw_json.dart';

class VerifyParameters {
  final String endpointUrl;
  final String token;
  final String applicant;
  final String apiKey;

  VerifyParameters({
    required this.endpointUrl,
    required this.token,
    required this.applicant,
    required this.apiKey,
  });

  RawJson toParam() {
    return {
      "endpointUrl": endpointUrl,
      "token": token,
      "applicant": applicant,
      "apiKey": apiKey,
    };
  }
}
