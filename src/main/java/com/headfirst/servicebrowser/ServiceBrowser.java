package com.headfirst.servicebrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tom on 8/3/2016.
 */
/*
Head First Java - Chapter 18, Remote deployment with RMI
 */
public class ServiceBrowser extends JPanel {

    private final JPanel widgetPane;
    private ServiceServer remoteStub;

    public ServiceBrowser() throws RMIException {
        remoteStub = (ServiceServer) getRemoteServiceStub(
                "rmi://127.0.0.1/service-server");

        int border = 10;
        JComboBox<String> serviceDropDown = new JComboBox<>(getServiceList());
        serviceDropDown.addItemListener((event) -> showSelectedService(event));

        widgetPane = new JPanel();
        widgetPane.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border
        ));
        widgetPane.setPreferredSize(new Dimension(500, 300));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setAlignmentX(BoxLayout.LINE_AXIS);
        center.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border));

        center.add(serviceDropDown);
        center.add(Box.createHorizontalStrut(10));
        center.add(widgetPane);

        add(center);
        showService((String) serviceDropDown.getSelectedItem());
    }

    public static void main(String[] args) throws RMIException {
        ServiceBrowser browser = new ServiceBrowser();
        GuiUtils.showInFrame("Service Browser", browser);
    }

    private Remote getRemoteServiceStub(String URL) throws RMIException {
        try {
            return Naming.lookup(URL);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            throw new RMIException("Remote service not available " + URL, ex);
        }
    }

    private String[] getServiceList() {
        if (remoteStub != null) {
            try {
                return remoteStub.getServiceList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return new String[]{""};
    }

    private void showService(String name) {
        try {
            Service serviceWidget = remoteStub.getService(name);
            widgetPane.removeAll();
            widgetPane.add(serviceWidget.getGuiPanel());
//            always call both revalidate and repaint
            revalidate();
            repaint();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showSelectedService(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            showService((String) itemEvent.getItem());
        }
    }


}
