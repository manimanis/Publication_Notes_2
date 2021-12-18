/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.frames;

import tn.manianis.XmlFile;

import javax.swing.*;

import tn.manianis.editors.TableCellEditorBase;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import tn.manianis.entities.Epreuve;
import tn.manianis.entities.EpreuvesCollection;
import tn.manianis.entities.Groupe;
import tn.manianis.entities.Note;
import tn.manianis.renderers.DefaultCellRenderer;
import tn.manianis.renderers.HeaderRenderer;
import tn.manianis.tablemodels.EleveRowTableModel;
import tn.manianis.utils.ComponentUtils;
import tn.manianis.utils.EditableCellFocusAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author manianis
 */
public class FeuilleNoteFrame extends JInternalFrame {

    private String filename;
    private EleveRowTableModel tableModel;
    private Groupe groupe;

    /**
     * Creates new form FeuilleNoteFrame
     */
    public FeuilleNoteFrame(String filename) throws Exception {
        super("", true, true, true, false);
        this.filename = filename;

        groupe = XmlFile.loadFile(filename);
        EpreuvesCollection epreuves = MainFrame.getConnection().fetchCoefficients(
                groupe.getDiscipline().getCodeMatiere(),
                groupe.getClasse().getCodeNiveau(),
                groupe.getPeriodeExamen().getCodePeriodeExamen());
        for (Epreuve epreuve : epreuves) {
            int p = groupe.getEpreuves().findByIds(
                    epreuve.getDiscipline().getCodeMatiere(),
                    epreuve.getCodeTypeMatiere(),
                    epreuve.getCodeTypeEpreuve()
            );
            if (p != -1) {
                groupe.getEpreuve(p).setCoefficient(epreuve.getCoefficient());
            }
        }
        groupe.getRowCollection().recalcAverages();

        tableModel = new EleveRowTableModel(groupe);
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                tableModelTableChanged(tableModelEvent);
                refreshButtons();
            }
        });

        initComponents();
        ComponentUtils.setOrientationRTL(rootPane);
        initTable();
        setTopPanelValues();
        setBottomPanelValues();
        setModeSaisie();
        refreshButtons();
    }

    /**
     * Init table
     */
    private void initTable() {
        JTableHeader header = rowTable.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(rowTable));
        setTableColumnsWidths();
        bindKeysActions();
    }

    /**
     * Set table columns widths and renderers
     */
    private void setTableColumnsWidths() {
        int colCount = rowTable.getColumnCount();
        rowTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        rowTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        rowTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        rowTable.getColumnModel().getColumn(colCount - 1).setPreferredWidth(200);
        for (int i = 0; i < colCount; i++) {
            TableColumn tc = rowTable.getColumnModel().getColumn(i);
            tc.setCellRenderer(new DefaultCellRenderer());
            tc.setCellEditor(new TableCellEditorBase());
            if (i > 2 && i < colCount - 1) {
                tc.setPreferredWidth(60);
            }
        }
    }

    /**
     * Bind the table used keys
     */
    private void bindKeysActions() {
        Action actionListener = (Action) rowTable.getActionForKeyStroke(KeyStroke.getKeyStroke("TAB"));
        rowTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        rowTable.getActionMap().put("Enter", actionListener);
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("ENTER"));
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("TAB"));
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("shift TAB"));
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("RIGHT"));
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("LEFT"));
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("UP"));
        new EditableCellFocusAction(rowTable, KeyStroke.getKeyStroke("DOWN"));
        rowTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");
        rowTable.getActionMap().put("F1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                shortcutKeyPressed("F1");
            }
        });
        rowTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "F2");
        rowTable.getActionMap().put("F2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                shortcutKeyPressed("F2");
            }
        });
        rowTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "F3");
        rowTable.getActionMap().put("F3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                shortcutKeyPressed("F3");
            }
        });
    }

    /**
     * Set the top panel values
     */
    private void setTopPanelValues() {
        setTitle(groupe.getClasse().getNomClasse() + " - " + groupe.getPeriodeExamen().getNomPeriodeExamen());
        txtClasse.setText(groupe.getClasse().getNomClasse());
        txtDRE.setText("ب" + groupe.getEtablissement().getDre().getNomDRE());
        txtEnseignant.setText(groupe.getEnseignant().getNomEnseignant());
        txtEtablissement.setText(groupe.getEtablissement().getNomEtablissement());
        txtMatiere.setText(groupe.getDiscipline().getNomMatiere());
        txtPeriode.setText(groupe.getPeriodeExamen().getNomPeriodeExamen());
    }

    /**
     * Set summaries values in the bottom panel
     */
    private void setBottomPanelValues() {
        Note[] summary = tableModel.calcSummary();
        txtAverage.setText(summary[0].toString());
        txtMax.setText(summary[1].toString());
        txtMin.setText(summary[2].toString());
    }
    
    private void refreshButtons() {
        btnCancelChanges.setEnabled(tableModel.isDirty());
        btnSave.setEnabled(tableModel.isDirty());
    }

    public String getFilename() {
        return filename;
    }

    public void setModeSaisie() {
        if (btnModeSaisie.isSelected()) {
            btnModeSaisie.setText("رقن الأعداد");
            tableModel.setEditNotes(true);
        } else {
            tableModel.setEditObservations(true);
            btnModeSaisie.setText("رقن الملاحظات");
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
        java.awt.GridBagConstraints gridBagConstraints;

        topPanel = new javax.swing.JPanel();
        lblDRE = new javax.swing.JLabel();
        txtDRE = new javax.swing.JLabel();
        lblEtablissement = new javax.swing.JLabel();
        txtEtablissement = new javax.swing.JLabel();
        lblPeriode = new javax.swing.JLabel();
        txtPeriode = new javax.swing.JLabel();
        lblClasse = new javax.swing.JLabel();
        txtClasse = new javax.swing.JLabel();
        lblMatiere = new javax.swing.JLabel();
        txtMatiere = new javax.swing.JLabel();
        lblEnseignant = new javax.swing.JLabel();
        txtEnseignant = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        rowTable = new javax.swing.JTable();
        bottomPanel = new javax.swing.JPanel();
        btnCalcObservations = new javax.swing.JButton();
        btnCoefficients = new javax.swing.JButton();
        btnModeSaisie = new javax.swing.JToggleButton();
        lblAverage = new javax.swing.JLabel();
        txtAverage = new javax.swing.JLabel();
        lblMax = new javax.swing.JLabel();
        txtMax = new javax.swing.JLabel();
        lblMin = new javax.swing.JLabel();
        txtMin = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnCancelChanges = new javax.swing.JButton();
        shortcutsPanel = new javax.swing.JPanel();
        labelsPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        chkImpObservation = new javax.swing.JCheckBox();
        chkImpMoyenne = new javax.swing.JCheckBox();
        btnListe = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        setMinimumSize(new java.awt.Dimension(400, 300));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {120, 200, 80, 240, 80, 240};
        jPanel1Layout.columnWeights = new double[] {8.0, 16.0, 8.0, 24.0, 8.0, 24.0};
        topPanel.setLayout(jPanel1Layout);

        lblDRE.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblDRE.setForeground(new java.awt.Color(102, 0, 0));
        lblDRE.setText("المندوبية الجهوية");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(lblDRE, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(txtDRE, gridBagConstraints);

        lblEtablissement.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblEtablissement.setForeground(new java.awt.Color(102, 0, 0));
        lblEtablissement.setText("المؤسسة");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(lblEtablissement, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(txtEtablissement, gridBagConstraints);

        lblPeriode.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblPeriode.setForeground(new java.awt.Color(102, 0, 0));
        lblPeriode.setText("الفترة");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(lblPeriode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(txtPeriode, gridBagConstraints);

        lblClasse.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblClasse.setForeground(new java.awt.Color(102, 0, 0));
        lblClasse.setText("القسم");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(lblClasse, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(txtClasse, gridBagConstraints);

        lblMatiere.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMatiere.setForeground(new java.awt.Color(102, 0, 0));
        lblMatiere.setText("المادة");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(lblMatiere, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(txtMatiere, gridBagConstraints);

        lblEnseignant.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblEnseignant.setForeground(new java.awt.Color(102, 0, 0));
        lblEnseignant.setText("الأستاذ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(lblEnseignant, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        topPanel.add(txtEnseignant, gridBagConstraints);

        getContentPane().add(topPanel, java.awt.BorderLayout.PAGE_START);

        rowTable.setAutoCreateRowSorter(true);
        rowTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rowTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rowTable.setModel(tableModel);
        rowTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        rowTable.setColumnSelectionAllowed(true);
        rowTable.setEditingColumn(20);
        rowTable.setEditingRow(20);
        rowTable.setIntercellSpacing(new java.awt.Dimension(2, 2));
        rowTable.setRowHeight(24);
        rowTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        rowTable.setShowGrid(true);
        rowTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rowTableKeyReleased(evt);
            }
        });
        scrollPane.setViewportView(rowTable);
        rowTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        java.awt.GridBagLayout bottomPanelLayout = new java.awt.GridBagLayout();
        bottomPanelLayout.columnWeights = new double[] {3.0, 3.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        bottomPanel.setLayout(bottomPanelLayout);

        btnCalcObservations.setText("حساب الملاحظات");
        btnCalcObservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcObservationsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(btnCalcObservations, gridBagConstraints);

        btnCoefficients.setText("الضوارب...");
        btnCoefficients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCoefficientsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(btnCoefficients, gridBagConstraints);

        btnModeSaisie.setSelected(true);
        btnModeSaisie.setText("رقن الأعداد");
        btnModeSaisie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnModeSaisieItemStateChanged(evt);
            }
        });
        bottomPanel.add(btnModeSaisie, new java.awt.GridBagConstraints());

        lblAverage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAverage.setForeground(new java.awt.Color(102, 0, 0));
        lblAverage.setText("معدّل القسم");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(lblAverage, gridBagConstraints);

        txtAverage.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(txtAverage, gridBagConstraints);

        lblMax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMax.setForeground(new java.awt.Color(102, 0, 0));
        lblMax.setText("أعلى معدل");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(lblMax, gridBagConstraints);

        txtMax.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(txtMax, gridBagConstraints);

        lblMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMin.setForeground(new java.awt.Color(102, 0, 0));
        lblMin.setText("أقل معدل");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(lblMin, gridBagConstraints);

        txtMin.setText("jLabel6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(txtMin, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(jSeparator1, gridBagConstraints);

        btnCancelChanges.setText("الرجوع عن التغييرات");
        btnCancelChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelChangesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(btnCancelChanges, gridBagConstraints);

        shortcutsPanel.setBorder(null);
        java.awt.GridBagLayout jPanel1Layout1 = new java.awt.GridBagLayout();
        jPanel1Layout1.columnWeights = new double[] {8.0, 1.0, 1.0};
        shortcutsPanel.setLayout(jPanel1Layout1);

        labelsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelsPanel.setLayout(new javax.swing.BoxLayout(labelsPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("الإختصارات");
        labelsPanel.add(jLabel7);

        jLabel8.setForeground(new java.awt.Color(102, 0, 102));
        jLabel8.setText("(F1) غياب شرعي - (F2) معفى - (F3) غير مسند");
        labelsPanel.add(jLabel8);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        shortcutsPanel.add(labelsPanel, gridBagConstraints);

        chkImpObservation.setSelected(true);
        chkImpObservation.setText("مع الملاحظة");
        shortcutsPanel.add(chkImpObservation, new java.awt.GridBagConstraints());

        chkImpMoyenne.setSelected(true);
        chkImpMoyenne.setText("مع المعدّل");
        shortcutsPanel.add(chkImpMoyenne, new java.awt.GridBagConstraints());

        btnListe.setText("قائمة الأعداد...");
        btnListe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        shortcutsPanel.add(btnListe, gridBagConstraints);

        btnSave.setText("حفظ");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        shortcutsPanel.add(btnSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        bottomPanel.add(shortcutsPanel, gridBagConstraints);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        setBounds(0, 0, 810, 530);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        XmlFile.saveFile(groupe, filename);
        tableModel.acceptChanges();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnListeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListeActionPerformed
        try {
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
            hashMap.put("imp_observation", chkImpObservation.isSelected());
            hashMap.put("imp_moy", chkImpMoyenne.isSelected());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(groupe.getRowCollection());
            InputStream is = getClass().getResourceAsStream("/publicationnotes_2/pkg0/feuille_notes.jasper");
            JasperPrint jp = JasperFillManager.fillReport(is, hashMap, dataSource);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(OtherDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OtherDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnListeActionPerformed

    private void btnCancelChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelChangesActionPerformed
        tableModel.rejectChanges();
    }//GEN-LAST:event_btnCancelChangesActionPerformed

    private void btnCoefficientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCoefficientsActionPerformed
        EpreuvesCollection epreuvesCopy = groupe.getEpreuves().cloneArray();
        EditCoefficientsFrame frame = new EditCoefficientsFrame(null, true);
        frame.setData(epreuvesCopy);
        frame.setVisible(true);
        if (frame.getDialogResult() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        groupe.setEpreuves(frame.getData());
        groupe.getRowCollection().recalcAverages();
        MainFrame.getConnection().updateCoefficients(
                groupe.getClasse().getCodeNiveau(),
                groupe.getPeriodeExamen().getCodePeriodeExamen(),
                groupe.getEpreuves());
        tableModel.fireTableDataChanged();
    }//GEN-LAST:event_btnCoefficientsActionPerformed

    private void btnCalcObservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcObservationsActionPerformed
        tableModel.calcObservations();
    }//GEN-LAST:event_btnCalcObservationsActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (tableModel.isDirty()) {
            int res = JOptionPane.showConfirmDialog(null, "لا بد من حفظ التغييرات قبل غلق النافذة.\nهل تريد حفظ التغييرات؟", getTitle(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
            if (res == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (res == JOptionPane.YES_OPTION) {
                btnSaveActionPerformed(null);
            }
        }
        setVisible(false);
        dispose();
    }//GEN-LAST:event_formInternalFrameClosing

    private void rowTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rowTableKeyReleased

    }//GEN-LAST:event_rowTableKeyReleased

    private void btnModeSaisieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnModeSaisieItemStateChanged
        setModeSaisie();
    }//GEN-LAST:event_btnModeSaisieItemStateChanged

    private void shortcutKeyPressed(String shortcutKeyName) {
        int selectedRow = rowTable.getSelectedRow();
        int selectedColumn = rowTable.getSelectedColumn();
        if (selectedRow == -1 || selectedColumn == -1) {
            return;
        }
        if (!tableModel.isNoteCell(selectedColumn)) {
            return;
        }
        int modelRow = rowTable.getRowSorter().convertRowIndexToModel(selectedRow);
        if (shortcutKeyName.equals("F1")) {
            tableModel.setMultipleColumns(modelRow,
                    selectedColumn,
                    selectedColumn, Note.ABSENT);
        } else if (shortcutKeyName.equals("F2")) {
            tableModel.setMultipleColumns(modelRow,
                    tableModel.getFirstNoteColumnIndex(),
                    tableModel.getLastNoteColumnIndex(), Note.DISPENSE);
        } else if (shortcutKeyName.equals("F3")) {
            tableModel.setMultipleColumns(modelRow,
                    tableModel.getFirstNoteColumnIndex(),
                    tableModel.getLastNoteColumnIndex(), Note.NON_AFFECTEE);
        }
    }

    private void tableModelTableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getType() == TableModelEvent.UPDATE) {
            String title = getTitle();
            if (tableModel.isDirty() && !title.endsWith(" *")) {
                title = title + " *";
                setTitle(title);
            } else if (!tableModel.isDirty() && title.endsWith(" *")) {
                title = title.substring(0, title.length() - 2);
                setTitle(title);
            }
        }
        setBottomPanelValues();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton btnCalcObservations;
    private javax.swing.JButton btnCancelChanges;
    private javax.swing.JButton btnCoefficients;
    private javax.swing.JButton btnListe;
    private javax.swing.JToggleButton btnModeSaisie;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkImpMoyenne;
    private javax.swing.JCheckBox chkImpObservation;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel labelsPanel;
    private javax.swing.JLabel lblAverage;
    private javax.swing.JLabel lblClasse;
    private javax.swing.JLabel lblDRE;
    private javax.swing.JLabel lblEnseignant;
    private javax.swing.JLabel lblEtablissement;
    private javax.swing.JLabel lblMatiere;
    private javax.swing.JLabel lblMax;
    private javax.swing.JLabel lblMin;
    private javax.swing.JLabel lblPeriode;
    private javax.swing.JTable rowTable;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel shortcutsPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel txtAverage;
    private javax.swing.JLabel txtClasse;
    private javax.swing.JLabel txtDRE;
    private javax.swing.JLabel txtEnseignant;
    private javax.swing.JLabel txtEtablissement;
    private javax.swing.JLabel txtMatiere;
    private javax.swing.JLabel txtMax;
    private javax.swing.JLabel txtMin;
    private javax.swing.JLabel txtPeriode;
    // End of variables declaration//GEN-END:variables

}
