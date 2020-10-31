import 'package:charts_flutter/flutter.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:stream_data/bloc/esp_bloc.dart';
import 'package:w3c_event_source/event_source.dart';
import 'package:flutter/material.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Esp data'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  EspBloc _espBloc = EspBloc();

  List<Series<Data, DateTime>> seriesList = [];
  List<Data> data = [];
  int count = 0;
  @override
  void initState() {
    super.initState();
    // _espBloc.httpConnect();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            BlocBuilder(
              cubit: _espBloc,
              builder: (context, state) {
                if (state is EspInitial) {
                  return Text("loading");
                } else if (state is EspTransmitting) {
                  // return Text(
                  //   '${state.data}',
                  //   style: Theme.of(context).textTheme.headline4,
                  // );
                  data.add(Data(DateTime.now(), double.parse(state.data)));
                  seriesList = [Series(
                    id: 'Esp',
                    data: data,
                    domainFn: (Data data,_) => data.index,
                    measureFn:  (Data data, _) => data.data,
                  )];
                  return Expanded(
                    child: TimeSeriesChart(
                      seriesList,
                      animate: true,
                    ),
                  );
                } else if (state is EspError) {
                  return Text("Error");
                } else if (state is EspDisconnected) {
                  return Text("Disconnected");
                } else if (state is EspConnecting) {
                  return Text("connecting");
                } else {
                  return Text('');
                }
              },
            ),
          ],
        ),
      ),
      floatingActionButton: BlocBuilder(
          cubit: _espBloc,
          builder: (context, state) {
            if (state is EspConnecting) {
              return FloatingActionButton(
                backgroundColor: Colors.grey,
                onPressed: null,
                tooltip: 'Increment',
                child: CircularProgressIndicator(
                  backgroundColor: Colors.white70,
                ),
              );
            } else if (state is EspConnected || state is EspTransmitting) {
              return FloatingActionButton(
                onPressed: () {
                  _espBloc.add(DisConnectEvent());
                },
                tooltip: 'Increment',
                child: Icon(Icons.wifi),
              );
            } else {
              return FloatingActionButton(
                onPressed: () {
                  _espBloc.add(ConnectEvent());
                },
                tooltip: 'Increment',
                child: Icon(Icons.wifi_off),
              );
            }
          }),
    );
  }
}

class Data {
  final DateTime index;
  final double data;

  Data(this.index, this.data);

}