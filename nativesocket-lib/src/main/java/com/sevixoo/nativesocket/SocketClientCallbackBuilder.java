package com.sevixoo.nativesocket;

/**
 * Created by pi19124 on 06.06.2017.
 */

public class SocketClientCallbackBuilder {

    public interface OnMessageListener{
        void onMessage(SocketConnection connection, String message);
    }

    public interface OnConnected{
        void onConnected(SocketConnection connection);
    }

    public interface OnDisconnected{
        void onDisconnected();
    }

    private OnMessageListener onMessageListener;
    private OnDisconnected onDisconnected;
    private OnConnected onConnected;

    SocketClientCallbackBuilder() {
    }

    public SocketClientCallbackBuilder message(OnMessageListener listener ){
        this.onMessageListener = listener;
        return this;
    }

    public SocketClientCallbackBuilder disconnected(OnDisconnected listener ){
        this.onDisconnected = listener;
        return this;
    }

    public SocketClientCallbackBuilder connected(OnConnected onConnected) {
        this.onConnected = onConnected;
        return this;
    }

    OnConnected getOnConnected() {
        return onConnected;
    }

    OnMessageListener getOnMessageListener() {
        return onMessageListener;
    }

    OnDisconnected getOnDisconnected() {
        return onDisconnected;
    }
}
