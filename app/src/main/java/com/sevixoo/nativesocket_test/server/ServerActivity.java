package com.sevixoo.nativesocket_test.server;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sevixoo.nativesocket.SocketConnection;
import com.sevixoo.nativesocket.SocketException;
import com.sevixoo.nativesocket.SocketServer;
import com.sevixoo.nativesocket.SocketServerCallbackBuilder;
import com.sevixoo.nativesocket_test.MainActivity;
import com.sevixoo.nativesocket_test.R;

public class ServerActivity extends Activity {

    private TextView    mMessageTextView;
    private Button      mButtonCreate;

    private SocketServer mSocketServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        mMessageTextView = (TextView)findViewById(R.id.message_text_view);
        mButtonCreate = (Button)findViewById(R.id.button_create);

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSocketServer == null){
                    try {
                        mSocketServer = new SocketServer(MainActivity.PORT);
                        onServerCreated();
                        mSocketServer.start();
                        displayMessage("listening started port=" + MainActivity.PORT);
                        mButtonCreate.setText("close");
                    } catch (SocketException e) {
                        e.printStackTrace();
                        displayMessage(e.getMessage());
                    }
                }else{
                    mButtonCreate.setText("start");
                    //TODO... close server
                }
            }
        });

    }

    public void onServerCreated(){
        mSocketServer.on().accept(new SocketServerCallbackBuilder.OnClientAcceptListener() {
            @Override
            public void onClientAccepted(SocketConnection connection){
                displayMessage("onClientAccepted");
                try {
                    connection.send("Hello from server!");
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
        mSocketServer.on().message(new SocketServerCallbackBuilder.OnMessage() {
            @Override
            public void onMessage(SocketConnection connection, String message) {
                displayMessage("onMessage:" + message);
                try {
                    connection.send(message);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
        mSocketServer.on().disconnected(new SocketServerCallbackBuilder.OnDisconnected() {
            @Override
            public void onDisconnected() {
                displayMessage("onDisconnected:");
            }
        });
    }

    private void displayMessage(final String message){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mMessageTextView.setText(mMessageTextView.getText()+"\n"+message);
                Log.e("[SERVER]",message);
            }
        });
    }

}
