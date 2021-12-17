/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.frames;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tn.manianis.XmlFile;
import tn.manianis.entities.Epreuve;
import tn.manianis.entities.Groupe;

/**
 *
 * @author manianis
 */
public class OtherDialog extends JFrame {

    /**
     * Creates new form OtherDialog
     */
    public OtherDialog() {
        try {
            initComponents();
            String filename = "/home/manianis/Desktop/Notes/MANI_MOHAMED ANIS/0077143288_31/0077143288_2001_31220203_31.xml";
            Groupe groupe = XmlFile.loadFile(filename);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(groupe.getRowCollection());
            HashMap<String, Object> hashMap = new HashMap<>();
            for (int i = 0; i < groupe.getEpreuves().size(); i++) {
                Epreuve epreuve = groupe.getEpreuve(i);
                hashMap.put(String.format("libnote%d", i), epreuve.getAbbrNomEpreuve());
            }
            hashMap.put("dre", groupe.getEtablissement().getDre().getNomDRE());
            hashMap.put("lycee", groupe.getEtablissement().getNomEtablissement());
            hashMap.put("matiere", groupe.getDiscipline().getNomMatiere());
            hashMap.put("trimestre", groupe.getPeriodeExamen().getNomPeriodeExamen());
            hashMap.put("classe", groupe.getClasse().getNomClasse());
            hashMap.put("enseignant", groupe.getEnseignant().getNomEnseignant());
            hashMap.put("annee_scolaire", String.format("%d/%d", groupe.getAnneeScolaire(), groupe.getAnneeScolaire() + 1));

            InputStream is = getClass().getResourceAsStream("/publicationnotes_2/pkg0/feuille_notes.jasper");
            JasperPrint jp = JasperFillManager.fillReport(is, hashMap, dataSource);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            Logger.getLogger(OtherDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OtherDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setSize(new java.awt.Dimension(508, 330));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OtherDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OtherDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OtherDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OtherDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OtherDialog dialog = new OtherDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
