/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicationnotes_2.pkg0;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import tn.manianis.frames.MainFrame;

import javax.swing.*;
import tn.manianis.XmlFilesFinder;

/**
 *
 * @author manianis
 */
public class PublicationNotes_20 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // XmlFilesFinder.findXmlFiles("/home/manianis/Desktop/2021-2022");
        Locale.setDefault(Locale.US);

        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PublicationNotes_20.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
        System.out.println("Welcome !");
    }
}
