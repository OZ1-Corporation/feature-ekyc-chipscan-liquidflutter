import 'raw_json.dart';

class Chip {
  final String name;
  final String birthday;
  final String address;
  final String sex;

  Chip({
    required this.name,
    required this.birthday,
    required this.address,
    required this.sex,
  });

  static Chip fromJson(RawJson raw) {
    return Chip(
      name: raw["name"],
      birthday: raw["birthday"],
      address: raw["address"],
      sex: raw["sex"],
    );
  }

  RawJson toJson() {
    return {
      "name": name,
      "birthday": birthday,
      "address": address,
      "sex": sex,
    };
  }
}
