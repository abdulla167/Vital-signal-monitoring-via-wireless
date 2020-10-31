part of 'esp_bloc.dart';

@immutable
abstract class EspEvent {}

class ConnectEvent extends EspEvent {}

class DataComeEvent extends EspEvent {
  final String data;

  DataComeEvent(this.data);
}

class DisConnectEvent extends EspEvent {}

class ErrorEvent extends EspEvent {}
