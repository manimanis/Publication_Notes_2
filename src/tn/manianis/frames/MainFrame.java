/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.frames;

import java.awt.Component;
import java.awt.Dimension;
import tn.manianis.entities.EpreuvesCollection;
import tn.manianis.entities.ObservationCollection;
import tn.manianis.utils.ComponentUtils;
import tn.manianis.utils.DbConnection;
import tn.manianis.utils.LRUFile;
import tn.manianis.utils.LRUFileCollection;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.h2.tools.Server;

/**
 * @author manianis
 */
public class MainFrame extends JFrame {

    private static final String LAST_USED_FOLDER = "LAST_USED_FOLDER";
    private LRUFileCollection lruFiles;
    private static DbConnection dbConnection;
    private static ObservationCollection observations;
    private ObservationsFrame observationsFrame = null;

    private Server serverTcp;
    private Server serverWeb;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        ComponentUtils.setOrientationRTL(this.rootPane);

        lruFiles = LRUFileCollection.loadLRU();
        refreshFileMenu();

        dbConnection = new DbConnection();
        try {
            dbConnection.open();
        } catch (SQLException ex) {
            System.err.println("Failed to connect to database: " + ex.getMessage());
            startServer();
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to connect to database: " + ex.getMessage());
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        fetchAllObservations();
    }

    public void startServer() {
        try {
            serverTcp = Server.createTcpServer("-ifNotExists", "-tcp", "-tcpAllowOthers");
            serverWeb = Server.createWebServer("-ifNotExists", "-web", "-webAllowOthers");
            serverTcp.start();
            serverWeb.start();
            System.out.println("Server started " + serverTcp.getStatus());
            System.out.println("Server started " + serverWeb.getStatus());
            dbConnection.open();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database : " + ex.getMessage());
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } catch (FileNotFoundException ex) {}
    }

    public void stopServer() {
        if (serverTcp != null && serverTcp.isRunning(true)) {
            serverTcp.stop();
            serverTcp.shutdown();
            System.out.println("Server shutdown!");
        }
        if (serverWeb != null && serverWeb.isRunning(true)) {
            serverWeb.stop();
            serverWeb.shutdown();
            System.out.println("Server shutdown!");
        }
    }

    public static ObservationCollection getObservations() {
        return observations;
    }

    public static void setObservations(ObservationCollection newObservations) {
        observations = newObservations;
    }

    public void fetchAllObservations() {
        observations = dbConnection.fetchObservations();
        if (observations.isEmpty()) {
            resetDefaultObservations();
        }
    }

    public void resetDefaultObservations() {
        observations = ObservationCollection.fillDefaultValues();
        dbConnection.deleteObservations();
        dbConnection.insertObservations(observations);
    }

    public static DbConnection getConnection() {
        return dbConnection;
    }

    public FeuilleNoteFrame createFeuilleNote(String filename) {
        try {
            FeuilleNoteFrame fn = new FeuilleNoteFrame(filename);
            fn.setVisible(true);
            desktopPane.add(fn);
            return fn;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
            lruFiles.remove(filename);
            LRUFileCollection.saveLRU(lruFiles);
            refreshFileMenu();
            return null;
        }
    }

    public FeuilleNoteFrame findFeuilleNoteByFilename(String filename) {
        for (JInternalFrame iframe : desktopPane.getAllFrames()) {
            if (iframe instanceof FeuilleNoteFrame) {
                FeuilleNoteFrame fn = (FeuilleNoteFrame) iframe;
                if (fn.getFilename().equalsIgnoreCase(filename)) {
                    return fn;
                }
            }
        }
        return null;
    }

    /**
     * Remove LRU Files items from the File Menu
     *
     * @param p1
     * @param p2
     */
    private void removeFileMenuLRUFiles(int p1, int p2) {
        for (int i = p2 - 1; i >= p1 + 1; i--) {
            fileMenu.remove(i);
        }
    }

    /**
     * Add LRU Files items to the file Menu
     *
     * @param p1
     */
    private void addFileMenuLRUFiles(int p1) {
        for (LRUFile lruFile : lruFiles) {
            JMenuItem menuItem = new JMenuItem();
            ComponentUtils.setOrientationRTL(menuItem);
            menuItem.setText(lruFile.getTitle());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    openFile(lruFile.getFilename());
                }
            });
            fileMenu.add(menuItem, p1);
            //p1++;
        }
    }

    /**
     * Refresh the LRU Files items in the file Menu
     */
    private void refreshFileMenu() {
        int p1 = ComponentUtils.findMenuItem(fileMenu, fileMenuSeparator1);
        int p2 = ComponentUtils.findMenuItem(fileMenu, fileMenuSeparator2);
        removeFileMenuLRUFiles(p1, p2);
        addFileMenuLRUFiles(p1 + 1);
    }

    public void openFiles() {
        Preferences prefs = Preferences.userRoot().node(getClass().getName());
        JFileChooser fileChooser = new JFileChooser(prefs.get(LAST_USED_FOLDER, FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Eduserv XML File", "xml"));
        fileChooser.setMultiSelectionEnabled(true);
        int r = fileChooser.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            prefs.put(LAST_USED_FOLDER, fileChooser.getSelectedFile().getParent());
            openFiles(fileChooser.getSelectedFiles());
        }
    }

    public void openFiles(File[] files) {
        for (File file : files) {
            openFile(file.getAbsolutePath());
        }
    }

    public void openFile(String filename) {
        FeuilleNoteFrame fn = findFeuilleNoteByFilename(filename);
        if (fn == null) {
            fn = createFeuilleNote(filename);
        }
        if (fn != null) {
            lruFiles.add(filename, fn.getTitle());
            LRUFileCollection.saveLRU(lruFiles);
            refreshFileMenu();
            ComponentUtils.toFront(fn);
        }
    }

    public void showObservationFrame() {
        observationsFrame = (ObservationsFrame) ComponentUtils.findByClass(desktopPane,
                ObservationsFrame.class);
        if (observationsFrame == null) {
            observationsFrame = new ObservationsFrame(this);
            observationsFrame.setVisible(true);
            desktopPane.add(observationsFrame);
            observationsFrame.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    closeObservationFrame();
                }
            });
        }
        ComponentUtils.toFront(observationsFrame);
    }

    public void closeObservationFrame() {
        if (observationsFrame != null && observationsFrame.isDirty()) {
            int res = JOptionPane.showConfirmDialog(null, "لا بد من حفظ التغييرات قبل غلق النافذة.\nهل تريد حفظ التغييرات؟", getTitle(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
            if (res == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (res == JOptionPane.YES_OPTION) {
                observations = observationsFrame.getTableModelData();
                dbConnection.deleteObservations();
                dbConnection.insertObservations(observations);
            }
        }
        observationsFrame.setVisible(false);
        observationsFrame.dispose();
    }

    public void exitApplication() {
        for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
            internalFrame.doDefaultCloseAction();
            if (internalFrame.isVisible()) {
                break;
            }
        }
        if (desktopPane.getAllFrames().length == 0) {
            dbConnection.close();
            setVisible(false);
            dispose();
            stopServer();
        }
    }

    private void populateWindowMenu() {
        for (int i = windowMenu.getMenuComponentCount() - 1; i >= 0; i--) {
            windowMenu.remove(i);
        }
        for (final JInternalFrame internalFrame : desktopPane.getAllFrames()) {
            JMenuItem menuItem = new JMenuItem(internalFrame.getTitle());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    ComponentUtils.toFront(internalFrame);
                }
            });
            windowMenu.add(menuItem);
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

        toolBar = new javax.swing.JToolBar();
        btnObservationFrame = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        desktopPane = new tn.manianis.components.DesktopScrollPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileOpen = new javax.swing.JMenuItem();
        fileMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        fileMenuSeparator2 = new javax.swing.JPopupMenu.Separator();
        fileExit = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        helpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("برنامج الأعداد");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        toolBar.setRollover(true);

        btnObservationFrame.setText("الملاحظات");
        btnObservationFrame.setFocusable(false);
        btnObservationFrame.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnObservationFrame.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnObservationFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObservationFrameActionPerformed(evt);
            }
        });
        toolBar.add(btnObservationFrame);

        getContentPane().add(toolBar, java.awt.BorderLayout.PAGE_START);

        scrollPane.setAutoscrolls(true);
        scrollPane.setViewportView(desktopPane);
        scrollPane.setViewportView(desktopPane);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        fileMenu.setText("ملف");

        fileOpen.setText("فتح ملف...");
        fileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileOpenActionPerformed(evt);
            }
        });
        fileMenu.add(fileOpen);
        fileMenu.add(fileMenuSeparator1);
        fileMenu.add(fileMenuSeparator2);

        fileExit.setText("خروج");
        fileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileExitActionPerformed(evt);
            }
        });
        fileMenu.add(fileExit);

        menuBar.add(fileMenu);

        windowMenu.setText("النوافذ");
        windowMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                windowMenuMenuSelected(evt);
            }
        });
        menuBar.add(windowMenu);

        helpMenu.setText("مساعدة");

        helpAbout.setText("حول البرنامج...");
        helpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpAboutActionPerformed(evt);
            }
        });
        helpMenu.add(helpAbout);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        setSize(new java.awt.Dimension(612, 431));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileOpenActionPerformed
        openFiles();
    }//GEN-LAST:event_fileOpenActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitApplication();
    }//GEN-LAST:event_formWindowClosing

    private void btnObservationFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObservationFrameActionPerformed
        showObservationFrame();
    }//GEN-LAST:event_btnObservationFrameActionPerformed

    private void fileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileExitActionPerformed
        exitApplication();
    }//GEN-LAST:event_fileExitActionPerformed

    private void windowMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_windowMenuMenuSelected
        populateWindowMenu();
    }//GEN-LAST:event_windowMenuMenuSelected

    private void helpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpAboutActionPerformed
        JOptionPane.showMessageDialog(rootPane, "برنامج من إعداد محمد انيس ماني", getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_helpAboutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnObservationFrame;
    private tn.manianis.components.DesktopScrollPane desktopPane;
    private javax.swing.JMenuItem fileExit;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileMenuSeparator1;
    private javax.swing.JPopupMenu.Separator fileMenuSeparator2;
    private javax.swing.JMenuItem fileOpen;
    private javax.swing.JMenuItem helpAbout;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JMenu windowMenu;
    // End of variables declaration//GEN-END:variables

}
