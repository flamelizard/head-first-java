package com.headfirst.servicebrowser;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tom on 7/25/2016.
 */
public interface ServiceServer extends Remote {
    public String[] getServiceList() throws RemoteException;
    public Service getService(String name) throws RemoteException;
}
