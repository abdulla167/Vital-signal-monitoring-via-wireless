import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';
import 'package:http/http.dart' as http;

part 'esp_event.dart';

part 'esp_state.dart';

class EspBloc extends Bloc<EspEvent, EspState> {
  EspBloc() : super(EspInitial());

  http.Client client;
  http.Request request;
  Future<http.StreamedResponse> response;
  StreamSubscription<String> _subscription;
  String _transmittedData = '';

  @override
  Stream<EspState> mapEventToState(
    EspEvent event,
  ) async* {
    if (event is ConnectEvent){
      yield EspConnecting();
      httpConnect();
      yield EspConnected();
    }
    else if (event is DataComeEvent){
      yield EspTransmitting(data: event.data);
    }
    else if (event is DisConnectEvent){
      await _subscription.cancel();
      client.close();

      yield EspDisconnected();
    }
  }

   httpConnect(){
    client =  http.Client();
    request = http.Request("GET", Uri.parse('http://10.0.3.2:8080/'));
    // request = http.Request("GET", Uri.parse('http://192.168.56.1:8080/'));
    request.headers["Accept"] = "text/event-stream";
    request.headers["Cache-Control"] = "no-cache";
    response = client.send(request);
    response.then((streamResponse){
      _subscription = utf8.decoder
          .bind(streamResponse.stream)
          .listen(_onData);
    });

  }

  _onData(String event) {
    _transmittedData += event;
    if(_transmittedData.contains("\n\n")){
      String data = _transmittedData.substring(_transmittedData.indexOf(":") + 1,_transmittedData.length-2);
      // double value = double.parse(data);
      _transmittedData = '';
      this.add(DataComeEvent(data));
    }
  }
}
