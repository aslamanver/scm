package com.aslam.scmessaging;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Add the below line to AndroidManifest.xml file
 * android:usesCleartextTraffic="true"
 */
public class SCMessaging {

    private Context context;
    private String serverURL;
    private String token;
    private Socket mSocket;
    private Listener listener;
    private boolean isDebug = true;

    public interface Listener {
        void onMessageData(String data);
    }

    public SCMessaging(Context context, String serverURL, String token) {
        this.context = context;
        this.serverURL = serverURL;
        this.token = token;
        this.listener = getDefaultListener();
    }

    private Listener getDefaultListener() {
        return new Listener() {
            @Override
            public void onMessageData(String data) {
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

    public void connect() throws URISyntaxException {
        mSocket = IO.socket(serverURL, socketOptions());
        setMessageListener();
        mSocket.connect();
    }

    private IO.Options socketOptions() {
        IO.Options opts = new IO.Options();
        opts.query = "token=" + token;
        opts.forceNew = true;
        return opts;
    }

    private void setMessageListener() {
        mSocket.on("push_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String data = "NO_DATA";
                try {
                    data = (String) args[0];
                } catch (Exception ex) {
                }
                log(data);
                listener.onMessageData(data);
                confirmDelivery(data);
            }
        });
    }

    private void confirmDelivery(String data) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("push_message_delivery", object.toString());
    }

    public void sendClientData(String clientData) {
        mSocket.emit("client_data", clientData);
    }

    public void disconnect() {
        mSocket.disconnect();
        mSocket.off("push_message");
    }

    public boolean connected() {
        return mSocket.connected();
    }

    public Socket getSocketEngine() {
        return mSocket;
    }
}
