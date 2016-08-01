package com.headfirst.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Tom on 7/21/2016.
 */
/*
Key points
- client needs to have remote interface class from server
- client makes a call on a stub (interface) as though it is an actual object
 */
public class RMIClient {
    public static void main(String[] args) {
        RMIClient client = new RMIClient();
        client.makeRemoteCalls();
    }

    public void makeRemoteCalls() {
        try {
            MyRemoteInterface stubRemoteObj = (MyRemoteInterface)
                    Naming.lookup("rmi://127.0.0.1/bob12");
            System.out.println(stubRemoteObj.getRemoteServerName());
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
