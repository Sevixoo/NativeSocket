package com.sevixoo.nativesocket;

/**
 * Created by pi19124 on 06.06.2017.
 */

public class SocketServerCallbackBuilder {

    public interface OnMessage{
        void onMessage(SocketConnection connection, String message);
    }

    public interface OnClientAcceptListener{
        void onClientAccepted(SocketConnection connection);
    }

    public interface OnDisconnected{
        void onDisconnected();
    }

    private OnClientAcceptListener onClientAcceptListener;
    private OnDisconnected onDisconnected;
    private OnMessage onMessage;

    SocketServerCallbackBuilder() {
    }

    public SocketServerCallbackBuilder accept( OnClientAcceptListener listener ){
        this.onClientAcceptListener = listener;
        return this;
    }

    public SocketServerCallbackBuilder disconnected( OnDisconnected listener ){
        this.onDisconnected = listener;
        return this;
    }

    public SocketServerCallbackBuilder message( OnMessage listener ){
        this.onMessage = listener;
        return this;
    }


    OnMessage getOnMessage() {
        return onMessage;
    }

    OnClientAcceptListener getOnClientAcceptListener() {
        return onClientAcceptListener;
    }

    OnDisconnected getOnDisconnected() {
        return onDisconnected;
    }
}
