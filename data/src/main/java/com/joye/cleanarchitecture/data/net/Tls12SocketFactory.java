package com.joye.cleanarchitecture.data.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * TLS1.2SocketFactory代理
 * <p>
 * Created by zczhang on 16/12/26.
 */

public class Tls12SocketFactory extends SSLSocketFactory {
    private String[] TLS_SUPPORT_VERSION = {"TLSv1.2", "TLSv1.1"};

    private final SSLSocketFactory delegate;

    public Tls12SocketFactory(SSLSocketFactory base) {
        this.delegate = base;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return patch(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return patch(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return patch(delegate.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return patch(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return patch(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket patch(Socket s) {
        if (s instanceof SSLSocket) {
            try {
                ((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);
            } catch (Exception e) {
                e.printStackTrace();
                //如果设置协议版本出现异常，则还原为原始版本，开发过程发现低版本系统(4.0)存在使用SSLv3版本的情况
                String[] originProtocols = ((SSLSocket) s).getSupportedProtocols();
                if (originProtocols != null) {
                    TLS_SUPPORT_VERSION = originProtocols;
                    ((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);
                }
            }
        }
        return s;
    }
}
