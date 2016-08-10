package com.headfirst.servicebrowser;

import com.headfirst.servicebrowser.services.DayOfTheWeekService;
import com.headfirst.servicebrowser.services.DiceService;

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
/*
Binary "rmiregistry" has to run in order to register service (RemoteObject)

Curiously, once the service has been registered, rmiregistry has to restart
to bind a service under the same name. Even though the original service has
been shut.
 */
public class ServiceServerImpl extends UnicastRemoteObject
        implements ServiceServer {
    private Map<String, Service> services = new HashMap<>();

    public ServiceServerImpl() throws RemoteException, RMIException {
        addServices();
        registerServiceServer("service-server");
    }

    public static void main(String[] args) {
        try {
            ServiceServerImpl server = new ServiceServerImpl();
        } catch (RemoteException | RMIException e) {
            e.printStackTrace();
        }
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
            throws RMIException {

        try {
            Naming.bind(asName, this);
        } catch (AlreadyBoundException | MalformedURLException | RemoteException e) {
            String msg = String.format(
                    "Register service %s: %s", asName, this.getClass());
            throw new RMIException(msg, e);
        }
    }

    private void addServices() {
//        every services has to implement Serializable for network transfer
        services.put("Dice", new DiceService());
        services.put("Day of the week", new DayOfTheWeekService());
    }
}
