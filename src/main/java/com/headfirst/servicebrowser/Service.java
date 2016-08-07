package com.headfirst.servicebrowser;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by Tom on 7/25/2016.
 */
public interface Service extends Serializable {
    public JPanel getGuiPanel();
}
