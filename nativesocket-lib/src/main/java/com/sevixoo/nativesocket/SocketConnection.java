package com.sevixoo.nativesocket;

import android.util.Log;

/**
 * Created by pi19124 on 06.06.2017.
 */

public class SocketConnection {

    static {
        System.loadLibrary("libSocketManager");
    }

    int mSocketId;

    SocketConnection(int socketId){
        this.mSocketId = socketId;
    }

    SocketConnection()throws SocketException {
        mSocketId = n_construct();
        if(mSocketId == -1){
            throw new SocketException("Could not create socket");
        }
    }

    public void send( String message )throws SocketException {
        int sendResult = n_send(mSocketId,message);
        if(sendResult < 0){
            throw new SocketException("Could not send message");
        }
    }

    public void close(){
        n_close(mSocketId);
    }

    int connect(String ipAddress, int port ){
        return n_connect(mSocketId,ipAddress,port);
    }

    String helloWorld(){
        return n_hello_world();
    }

    String read()throws SocketException{
        String message = n_read(mSocketId);
        if(message==null){
            throw new SocketException("Could not read");
        }
        return message;
    }

    int listen(int port) {
        return n_listen(mSocketId,port);
    }

    SocketConnection accept()throws SocketException {
        int newSocketId = n_accept(mSocketId);
        if(newSocketId < 0){
            throw new SocketException("Accept fail");
        }else{
            return new SocketConnection(newSocketId);
        }
    }

      /* native API */

    private native String n_hello_world();

    private native int n_construct();

    private native int n_connect(int sockId, String ipAddress, int port );

    private native void n_broadcast();

    private native int n_send(int sockId, String message);

    private native void n_close(int sockId);

    private native int n_listen(int sockId, int port);

    private native int n_accept(int sockId);

    private native String n_read(int sockId);


    public boolean isConnected() {
        return mSocketId > 0;
    }
}
