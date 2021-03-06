package com.headfirst.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tom on 7/20/2016.
 */
/*
 Requirements to successfully register a service (remote object)
 - stub class for the service generated by manually running "rmic"
 - stub class has to be in correct path - where implementation class file
 resides
 - "rmiregistry" runs at the **root** of the project (e.g folder up top level
  such as "com")
 */
public class RMIServerDaemon {

    public static void main(String[] args) throws RemoteException {
        MyRemoteImpl remoteService = new MyRemoteImpl();
        RMIServerDaemon.registerService("bob12", remoteService);
    }

    public static void registerService(String name, Remote service) {
        try {
            Naming.rebind(name, service);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
