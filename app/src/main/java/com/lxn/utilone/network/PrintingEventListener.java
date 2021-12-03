package com.lxn.utilone.network;

import com.lxn.utilone.util.DateUtil;
import com.lxn.utilone.util.toolutil.TimeUtil;

import okhttp3.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
  *  @author lixiaonan
  *  功能描述: 监听相关事件的
  *  时 间： 2020/8/18 3:54 PM
  */
public class PrintingEventListener extends EventListener {
    private long callStartNanos;

    private void printEvent(String name) {
        long nowNanos = System.nanoTime();
        if (name.equals("callStart")) {
            callStartNanos = nowNanos;
        }
        long elapsedNanos = nowNanos - callStartNanos;
        System.out.printf(DateUtil.getNowDateDetail()+"====%.3f %s%n", elapsedNanos / 1000000000d, name);
    }

    @Override public void callStart(Call call) {
        printEvent("callStart");
    }

    @Override public void callEnd(Call call) {
        printEvent("callEnd");
    }

    @Override public void dnsStart(Call call, String domainName) {
        printEvent("dnsStart");
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        printEvent("dnsEnd");
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        if(inetSocketAddress!=null){
            printEvent("connectStart==="+inetSocketAddress.toString()+"==="+inetSocketAddress.hashCode());
        }else{
            printEvent("connectStart===");
        }

    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        printEvent("secureConnectStart");
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        printEvent("secureConnectEnd");
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        printEvent("connectEnd==="+inetSocketAddress.toString()+"+==="+inetSocketAddress.hashCode());
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        printEvent("connectionAcquired=获取之后的=="+connection.toString()+"=="+connection.socket().toString()+"==="+connection.route().toString());
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        printEvent("connectionReleased=释放之后的=="+connection.toString()+"=="+connection.hashCode());
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        printEvent("requestBodyEnd 请求开始 ");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        printEvent("requestBodyEnd 请求结束 ");
    }

    @Override
    public void requestFailed(Call call, IOException ioe) {
        super.requestFailed(call, ioe);
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        printEvent("responseBodyStart 回应开始");
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        printEvent("responseBodyStart 回应结束");
    }

    @Override
    public void responseFailed(Call call, IOException ioe) {
        super.responseFailed(call, ioe);
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
    }
}
