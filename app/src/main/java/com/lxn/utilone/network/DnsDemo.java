package com.lxn.utilone.network;

import okhttp3.Dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
  *  @author lixiaonan
  *  功能描述: 域名解析的
  *  时 间： 2021/2/3 8:54 PM
  */
public class DnsDemo implements Dns{
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        System.out.println("==调用域名解析的=="+hostname);
        return Dns.SYSTEM.lookup(hostname);
    }
}
