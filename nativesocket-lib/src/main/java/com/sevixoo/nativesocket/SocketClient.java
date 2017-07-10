package com.sevixoo.nativesocket;

import android.util.Log;

/**
 * Created by pi19124 on 06.06.2017.
 */

public class SocketClient{

    private SocketConnection mSocketConnection;

    private SocketClientCallbackBuilder mSocketClientCallback;

    private Thread readThread;

    private boolean isConnected;

    public SocketClient() throws SocketException {
        mSocketConnection = new SocketConnection();
        mSocketClientCallback = new SocketClientCallbackBuilder();
    }

    public String helloWorld() {
        return mSocketConnection.helloWorld();
    }

    public void connect( String serverAddress , int port )throws SocketException{
        int connectResult = mSocketConnection.connect( serverAddress , port);
        if(connectResult == -1){
            throw new SocketException("Could not connect");
        }
        mSocketClientCallback.getOnConnected().onConnected(mSocketConnection);
        isConnected = true;
        readThread = new Thread(){
            @Override
            public void run() {
                while (isConnected){
                    try {
                        String message = mSocketConnection.read();
                        if(message != null && !message.equals("")){
                            mSocketClientCallback.getOnMessageListener().onMessage(mSocketConnection,message);
                            Log.e("SocketClient","on message:" + message);
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                        isConnected = false;
                    }
                }
            }
        };
        readThread.start();

    }

    public SocketClientCallbackBuilder on(){
        return mSocketClientCallback;
    }

}
