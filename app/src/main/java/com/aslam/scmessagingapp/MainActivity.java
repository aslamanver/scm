package com.aslam.scmessagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aslam.scmessaging.SCMessaging;
import com.aslam.scmessagingapp.databinding.ActivityMainBinding;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SCMessaging scMessaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.contains("server_url")) {
            preferences.edit().putString("server_url", "http://192.168.8.200:3000").commit();
        }

        if (!preferences.contains("token")) {
            preferences.edit().putString("token", "APP-1").commit();
        }

        binding.edtServer.setText(preferences.getString("server_url", ""));
        binding.edtToken.setText(preferences.getString("token", ""));

        binding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String serverURL = binding.edtServer.getText().toString().trim();
                final String token = binding.edtToken.getText().toString().trim();

                if (serverURL.length() < 5) {
                    uiLog("URL is not valid");
                    return;
                }

                if (token.length() < 2) {
                    uiLog("Token is not valid");
                    return;
                }

                preferences.edit().putString("server_url", serverURL).commit();
                preferences.edit().putString("token", token).commit();

                if (scMessaging != null) {
                    scMessaging.disconnect();
                    scMessaging = null;
                }

                uiLog("Staring...");

                scMessaging = new SCMessaging(getApplicationContext(), serverURL, token);

                scMessaging.setListener(new SCMessaging.Listener() {
                    @Override
                    public void onMessageData(final String data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uiLog("onMessageData => " + data);
                            }
                        });
                    }
                });

                try {
                    binding.btnConnect.setEnabled(false);
                    scMessaging.connect();
                    uiLog("Connecting...");
                    final Handler connectionHandler = new Handler();
                    connectionHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (scMessaging != null && scMessaging.connected()) {
                                uiLog("Connected.");
                                binding.btnConnect.setEnabled(true);
                                connectionHandler.removeCallbacks(this);
                                return;
                            }
                            connectionHandler.postDelayed(this, 1000);
                            uiLog("Retrying...");
                        }
                    }, 1000);
                } catch (URISyntaxException e) {
                    uiLog(e.getMessage());
                }
            }
        });
    }

    private void uiLog(String data) {
        binding.txtLog.setText(data + "\n" + binding.txtLog.getText().toString());
    }
}
