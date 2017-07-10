package com.sevixoo.nativesocket_test.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sevixoo.nativesocket.SocketClient;
import com.sevixoo.nativesocket.SocketClientCallbackBuilder;
import com.sevixoo.nativesocket.SocketConnection;
import com.sevixoo.nativesocket.SocketException;
import com.sevixoo.nativesocket_test.MainActivity;
import com.sevixoo.nativesocket_test.R;
import com.sevixoo.nativesocket_test.service.LanService;

public class ClientActivity extends Activity {

    private TextView    mMessageTextView;
    private Button      mButtonConnect;
    private Button      mButtonSend;
    private EditText    mEditTextMessage;

    private SocketClient mSocketClient;
    private LanService mLanService;
    private SocketConnection mSocketConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        mLanService = new LanService(getBaseContext());

        mButtonSend = (Button)findViewById(R.id.button_send);
        mEditTextMessage = (EditText)findViewById(R.id.editText_message);
        mMessageTextView = (TextView)findViewById(R.id.message_text_view);
        mButtonConnect = (Button)findViewById(R.id.button_create);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSocketClient == null) {
                    try {
                        mSocketClient = new SocketClient();
                        onClientCreated();
                        String myIp = mLanService.getIpAddress();
                        String ipAddress = mLanService.getIpAddress();
                        ipAddress = ipAddress.substring(0,11);
                        boolean isConnected = false;
                        for( int i = 1 ; i < 255 && !isConnected ; i++ ){
                            if(ipAddress.equals(myIp)){
                                continue;
                            }
                            String adr = ipAddress + i;
                            displayMessage("ipAddress:"+adr);
                            try {
                                mSocketClient.connect(adr, MainActivity.PORT);
                                displayMessage("connected:"+adr);
                                isConnected = true;
                            }catch (SocketException e) {
                                e.printStackTrace();
                            }
                        }

                        mButtonConnect.setText("close");
                    } catch (SocketException e) {
                        e.printStackTrace();
                        displayMessage(e.getMessage());
                    }
                }else{
                    mButtonConnect.setText("connect");
                    //TODO... close client
                }
            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSocketClient !=null){
                    try {
                        mSocketConnection.send(mEditTextMessage.getText().toString());
                        displayMessage("send:" + mEditTextMessage.getText());
                        mEditTextMessage.setText("");
                    } catch (SocketException e) {
                        e.printStackTrace();
                        displayMessage(e.getMessage());
                    }
                }
            }
        });

    }

    public void onClientCreated(){
        mSocketClient.on().message(new SocketClientCallbackBuilder.OnMessageListener() {
            @Override
            public void onMessage(SocketConnection connection, String message) {
                displayMessage("onMessage:" + message);
            }
        }).disconnected(new SocketClientCallbackBuilder.OnDisconnected() {
            @Override
            public void onDisconnected() {
                displayMessage("onDisconnected:" );
            }
        }).connected(new SocketClientCallbackBuilder.OnConnected() {
            @Override
            public void onConnected(SocketConnection connection) {
                mSocketConnection = connection;
                displayMessage("connectedToServer");
            }
        });
    }


    private void displayMessage(final String message){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mMessageTextView.setText(mMessageTextView.getText()+"\n"+message);
                Log.e("[CLIENT]",message);
            }
        });
    }

}
