package com.sevixoo.nativesocket;

import java.io.IOException;

/**
 * Created by pi19124 on 06.06.2017.
 */

public class SocketException extends IOException {

    public SocketException() { }

    public SocketException(String message) {
        super(message);
    }
}
