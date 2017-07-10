package com.sevixoo.nativesocket_test;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sevixoo.nativesocket.SocketClient;
import com.sevixoo.nativesocket.SocketClientCallbackBuilder;
import com.sevixoo.nativesocket.SocketConnection;
import com.sevixoo.nativesocket.SocketException;
import com.sevixoo.nativesocket.SocketServer;
import com.sevixoo.nativesocket.SocketServerCallbackBuilder;
import com.sevixoo.nativesocket_test.client.ClientActivity;
import com.sevixoo.nativesocket_test.server.ServerActivity;
import com.sevixoo.nativesocket_test.service.LanService;

public class MainActivity extends Activity {


    public final static int PORT = 8282;

    private Button mButtonIpTest;
    private Button mButtonServer;
    private Button mButtonClient;

    private TextView mMessageTextView;

    private LanService mLanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLanService = new LanService(getBaseContext());

        mMessageTextView = (TextView)findViewById(R.id.message_text_view);
        mButtonIpTest = (Button)findViewById(R.id.button_check);
        mButtonServer = (Button)findViewById(R.id.button_server);
        mButtonClient = (Button)findViewById(R.id.button_client);

        mButtonIpTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMessage("ipAddress=" + mLanService.getIpAddress());
            }
        });

        mButtonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getBaseContext(), ServerActivity.class);
                startActivity(intent);
            }
        });

        mButtonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getBaseContext(), ClientActivity.class);
                startActivity(intent);
            }
        });
    }

    public void displayMessage(String message){
        mMessageTextView.setText(message);
    }

}
