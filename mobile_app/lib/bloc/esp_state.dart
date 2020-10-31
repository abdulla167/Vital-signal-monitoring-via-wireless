part of 'esp_bloc.dart';

@immutable
abstract class EspState {}

class EspInitial extends EspState {
    final message = "Connecting";
}

class EspConnecting extends EspState {}

class EspConnected extends EspState {}

class EspTransmitting extends EspState {

  final String data;

  EspTransmitting({this.data});
}



class EspDisconnected extends EspState {
  final message = "Disconnected";
}

class EspError extends EspState {}