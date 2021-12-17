/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.utils;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author manianis
 */
public class ComponentUtils {
    public static void setOrientationRTL(JComponent component) {
        component.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Component[] compenents = component.getComponents();
        if (component instanceof JMenu) {
           compenents = ((JMenu) component).getMenuComponents();
        }
        
        for (Component cmp : compenents) {
            if (cmp instanceof JComponent) {
                setOrientationRTL((JComponent) cmp);
            }
        }
    }

    public static int findMenuItem(JMenu menu, JComponent menuItem) {
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            if (menu.getMenuComponent(i) == menuItem) {
                return i;
            }
        }
        return -1;
    }
    
    public static JInternalFrame findByClass(JDesktopPane desktopPane, Class classe) {
        for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
            if (internalFrame.getClass().equals(classe)) {
                return internalFrame;
            }
        }
        return null;
    }
    
    public static void toFront(JInternalFrame internalFrame) {
        try {
            internalFrame.toFront();
            internalFrame.setSelected(true);
        } catch (PropertyVetoException ex) {}
    }
}
