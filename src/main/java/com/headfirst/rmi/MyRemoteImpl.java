package com.headfirst.rmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Tom on 7/20/2016.
 */
/*
Remote object (service) class
- this object is called remotely by a client
 */
public class MyRemoteImpl
        extends UnicastRemoteObject implements MyRemoteInterface {

    public MyRemoteImpl() throws RemoteException {
    }

    @Override
    public String getRemoteServerName() throws RemoteException {
        String hostname = "unknown";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignore) {
        }
        return hostname;
    }
}
