package com.aslam.scmessagingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aslam.scmessaging.SCMService;
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

        // Simple Usage
        // scMessaging = new SCMessaging(getApplicationContext(), "http://192.168.8.200:3000", "Token-1");
        //     scMessaging.setListener(new SCMessaging.Listener() {
        //     @Override
        //     public void onMessageData(final String data) {
        //         runOnUiThread(new Runnable() {
        //             @Override
        //             public void run() {
        //                 uiLog(data);
        //             }
        //         });
        //     }
        // });
        // scMessaging.connect();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.contains("server_url")) {
            preferences.edit().putString("server_url", "http://192.168.8.200:3000").commit();
        }

        if (!preferences.contains("token")) {
            preferences.edit().putString("token", "APP-1").commit();
        }

        binding.edtServer.setText(preferences.getString("server_url", ""));
        binding.edtToken.setText(preferences.getString("token", ""));

        binding.btnDisconnect.setEnabled(false);

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

                try {

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

                        @Override
                        public void onConnect(final String serverURL) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    uiLog("onConnect => " + serverURL);
                                    binding.btnDisconnect.setEnabled(true);
                                    binding.btnConnect.setEnabled(false);
                                }
                            });
                        }

                        @Override
                        public void onDisconnect(final String serverURL) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    uiLog("onDisconnect => " + serverURL);
                                    binding.btnConnect.setEnabled(true);
                                    binding.btnDisconnect.setEnabled(false);
                                }
                            });
                        }

                        @Override
                        public void onConnectError(final Exception ex) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.btnConnect.setEnabled(true);
                                    uiLog("onConnectError => " + ex.getCause().getMessage());
                                }
                            });
                        }
                    });

                    scMessaging.connect();

                    uiLog("Connecting...");
                    binding.btnConnect.setEnabled(false);
                    binding.btnDisconnect.setEnabled(false);

                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }

            }
        });

        binding.btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scMessaging != null) {
                    scMessaging.disconnect();
                }
            }
        });

        binding.btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCMService.start(getApplicationContext(), MySCMService.class);
            }
        });
    }

    private void uiLog(String data) {
        binding.txtLog.setText(data + "\n" + binding.txtLog.getText().toString());
    }
}
