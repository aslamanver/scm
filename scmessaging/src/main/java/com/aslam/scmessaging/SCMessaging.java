package com.aslam.scmessaging;

import android.content.Context;
import android.util.Log;

// import com.github.nkzawa.emitter.Emitter;
// import com.github.nkzawa.socketio.client.IO;
// import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Add the below line to AndroidManifest.xml file
 * android:usesCleartextTraffic="true"
 */
public class SCMessaging {

    private Context context;
    private String serverURL;
    private String token;
    private Socket mSocket;
    private IO.Options options;
    private Listener listener;
    private boolean isDebug = true;

    public abstract static class Listener {

        public abstract void onMessageData(String data);

        public void onConnect(String serverURL) {
        }

        public void onDisconnect(String serverURL) {
        }

        public void onConnectError(Exception ex) {
        }
    }

    public SCMessaging(Context context, String serverURL, String token) throws URISyntaxException {
        init(context, serverURL, token);
        initSocket(new IO.Options());
    }

    public SCMessaging(Context context, String serverURL, String token, IO.Options options) throws URISyntaxException {
        init(context, serverURL, token);
        initSocket(options);
    }

    private void initSocket(IO.Options opts) throws URISyntaxException {
        options = opts;
        setDefaultOptions();
        mSocket = IO.socket(serverURL, options);
    }

    private void init(Context context, String serverURL, String token) {
        this.context = context;
        this.serverURL = serverURL;
        this.token = token;
        this.listener = getDefaultListener();
    }

    private Listener getDefaultListener() {
        return new Listener() {
            @Override
            public void onMessageData(String data) {
                log("onMessageData => " + data);
            }
        };
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDebug(boolean status) {
        this.isDebug = status;
    }

    private void log(String data) {
        if (isDebug) {
            Log.d("SCMessaging", data);
        }
    }

    public void connect() {
        setMessageListener();
        mSocket.connect();
    }

    private void setDefaultOptions() {
        options.query = "token=" + token;
        options.forceNew = true;
        SocketSSL.set(options);
    }

    private void setMessageListener() {

        mSocket.on("push_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String data = "NO_DATA";
                try {
                    if(args[0] instanceof JSONObject) {
                        data = args[0].toString();
                    } else {
                        data = (String) args[0];
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                log("onMessageData => " + data);
                listener.onMessageData(data);
                confirmDelivery(data);
            }
        });

        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                listener.onDisconnect(serverURL);
            }
        });

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                listener.onConnect(serverURL);
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                listener.onConnectError((Exception) args[0]);
            }
        });
    }

    private void confirmDelivery(String data) {
        mSocket.emit("push_message_delivery", getJSONData(data).toString());
    }

    public void sendClientData(String data) {
        mSocket.emit("client_data", getJSONData(data).toString());
    }

    public void disconnect() {
        mSocket.disconnect();
        mSocket.off("push_message");
        mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off(Socket.EVENT_DISCONNECT);
        mSocket.off(Socket.EVENT_CONNECT_ERROR);
        listener.onDisconnect(serverURL);
    }

    public boolean connected() {
        return mSocket.connected();
    }

    public Socket getSocketEngine() {
        return mSocket;
    }

    private JSONObject getJSONData(String data) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
