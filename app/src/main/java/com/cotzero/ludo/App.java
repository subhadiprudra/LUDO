package com.cotzero.ludo;

import android.app.Application;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class App extends Application {
    public static Socket socket;
    public static App MyApp;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp = this;
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            socket = IO.socket("https://ludoappsubhadip.herokuapp.com",opts);

            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.i("error",e.getMessage());
        }
    }

    public static Socket getSocket(){
        return socket;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("socket", "connected...");
            // This doesn't run in the UI thread, so use:
            // .runOnUiThread if you want to do something in the UI

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("socket", "Error connecting...");
        }
    };
}
