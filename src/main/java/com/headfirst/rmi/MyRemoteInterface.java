package com.headfirst.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tom on 7/20/2016.
 */
public interface MyRemoteInterface extends Remote {
    //    define remote service methods
    public String getRemoteServerName() throws RemoteException;
}
