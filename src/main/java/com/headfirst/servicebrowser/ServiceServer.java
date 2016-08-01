package com.headfirst.servicebrowser;

import java.rmi.Remote;

/**
 * Created by Tom on 7/25/2016.
 */
public interface ServiceServer extends Remote {
    public String[] getServiceList();

    public Service getService(String name);
}
