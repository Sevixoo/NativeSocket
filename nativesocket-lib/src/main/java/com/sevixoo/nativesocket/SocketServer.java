package com.sevixoo.nativesocket;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pi19124 on 06.06.2017.
 */

public class SocketServer{

    private Thread acceptThread;

    private boolean isListening = false;

    private SocketConnection mSocketConnection;

    private List<SocketConnection> clientConnections = new ArrayList<>();

    private SocketServerCallbackBuilder mSocketServerCallback;

    public SocketServer(int port) throws SocketException {
        mSocketConnection = new SocketConnection();
        mSocketServerCallback = new SocketServerCallbackBuilder();
        int listenResult = mSocketConnection.listen(port);
        if(listenResult < 0){
            throw new SocketException("Could not start listen");
        }
    }

    public synchronized void start(){
        if(!isListening){
            isListening = true;
            acceptThread = new Thread(){
                @Override
                public void run() {
                    while (isListening){
                        try {
                            SocketConnection socketConnection = mSocketConnection.accept();
                            clientConnections.add(socketConnection);
                            startListenThread(socketConnection);
                            mSocketServerCallback.getOnClientAcceptListener().onClientAccepted(socketConnection);
                        } catch (SocketException e) {
                            e.printStackTrace();
                            isListening = false;
                        }

                    }
                }
            };
            acceptThread.start();
        }
    }

    private Thread mReadThread;

    private void startListenThread(final SocketConnection socketConnection) {
        mReadThread = new Thread(){
            @Override
            public void run() {
                SocketConnection socket = socketConnection;
                while (socket.isConnected()){
                    try {
                        String message = socket.read();
                        if(!message.equals("")){
                            mSocketServerCallback.getOnMessage().onMessage(socket,message);
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mReadThread.start();
    }

    public synchronized void stop(){
        isListening = false;
        acceptThread = null;
    }

    public SocketServerCallbackBuilder on(){
        return mSocketServerCallback;
    }

}
