package com.headfirst.servicebrowser;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by Tom on 7/25/2016.
 */
/*
All class instance variables becomes serializable when the class implements
Serializable interface or an interface that extends Serializable.
 */
public interface Service extends Serializable {
    JPanel getGuiPanel();
}
