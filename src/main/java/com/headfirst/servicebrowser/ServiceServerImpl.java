package com.headfirst.servicebrowser;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 7/25/2016.
 */
public class ServiceServerImpl extends UnicastRemoteObject
        implements ServiceServer {
    private Map<String, Service> services = new HashMap<>();

    public ServiceServerImpl() throws RemoteException, RegisterException {
        registerServiceServer("service-server");

    }

    @Override
    public String[] getServiceList() {
        return services.keySet().toArray(new String[]{});
    }

    @Override
    public Service getService(String name) {
        return services.getOrDefault(name, null);
    }

    private void registerServiceServer(String asName)
            throws RegisterException {

        try {
            Naming.bind(asName, this);
        } catch (AlreadyBoundException | MalformedURLException | RemoteException e) {
            String msg = String.format("%s: %s", asName, this.getClass());
            throw new RegisterException(msg, e);
        }
    }
}
