#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "include/AndroidDebug.h"

const int BUFFER_SIZE = 16;

extern "C"
{

    jstring Java_com_sevixoo_nativesocket_SocketConnection_n_1hello_1world(JNIEnv *env, jobject /* this */)
    {
        std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
    }

    jint Java_com_sevixoo_nativesocket_SocketConnection_n_1construct(JNIEnv *env, jobject /* this */)
    {
        int socketId = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

        return socketId;
    }

    jint Java_com_sevixoo_nativesocket_SocketConnection_n_1connect(JNIEnv *env, jobject /* this */ , jint socketId, jstring ip_address, jint port)
    {
        const char *arg_ip_address = env->GetStringUTFChars(ip_address, 0);

        struct sockaddr_in server_addr;

        bzero((char *) &server_addr, sizeof(server_addr));
        server_addr.sin_family = AF_INET;
        server_addr.sin_addr.s_addr = inet_addr(arg_ip_address);
        server_addr.sin_port = htons(port);

        if(inet_pton(AF_INET, arg_ip_address, &server_addr.sin_addr)<=0)
        {
            printf("\n inet_pton error occured\n");
            return 0;
        }

        jint connectResult = connect(socketId,(struct sockaddr *)&server_addr,sizeof(server_addr));
        return connectResult;
    }

    void Java_com_sevixoo_nativesocket_SocketConnection_n_1broadcast(JNIEnv *env, jobject /* this */)
    {

    }

    jint Java_com_sevixoo_nativesocket_SocketConnection_n_1send(JNIEnv *env, jobject /* this */ , jint socketId, jstring message)
    {
        char out_buffer[BUFFER_SIZE];
        bzero(out_buffer,BUFFER_SIZE);

        const char* out_message = env->GetStringUTFChars(message, 0);;
        strcpy( out_buffer, out_message );
        jint writeResult = write(socketId,out_buffer,strlen(out_buffer));
        LOGE( "socketSend %s length=%d" ,out_buffer , writeResult );
        //env->ReleaseStringUTFChars(message, out_message);
        return writeResult;
    }

    void Java_com_sevixoo_nativesocket_SocketConnection_n_1close(JNIEnv *env, jobject /* this */, jint socketId)
    {
        close(socketId);
    }

    jint Java_com_sevixoo_nativesocket_SocketConnection_n_1listen(JNIEnv *env, jobject /* this */ , jint socketId, jint port )
    {
        struct sockaddr_in server_addr;

        bzero((char *) &server_addr, sizeof(server_addr));
        server_addr.sin_family = AF_INET;
        server_addr.sin_port = htons(port);
        server_addr.sin_addr.s_addr = INADDR_ANY;

        jint bindResult = bind(socketId, (struct sockaddr *) &server_addr, sizeof(server_addr));
        if( bindResult < 0 ){
            return -1;
        }
        listen(socketId,5);
        return 0;
    }

    jint Java_com_sevixoo_nativesocket_SocketConnection_n_1accept(JNIEnv *env, jobject /* this */ , jint socketId )
    {
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);
        jint new_socketId = accept(socketId, (struct sockaddr *) &client_addr, &client_len);
        return new_socketId;
    }

    jstring Java_com_sevixoo_nativesocket_SocketConnection_n_1read(JNIEnv *env, jobject /* this */ , jint socketId )
    {
        char out_buffer[BUFFER_SIZE];
        bzero(out_buffer,BUFFER_SIZE);

        LOGE("read start");

        int readResult = read(socketId,out_buffer,BUFFER_SIZE-1);
        out_buffer[readResult] = 0;
        if(readResult < 0){
            return NULL;
        }else if(readResult > 0){
            LOGE("readResult %d %s", readResult, out_buffer);
        }else{
            LOGE("readResult %d ", readResult );
        }
        jstring message = env->NewStringUTF(out_buffer);
        return message;
    }

}
