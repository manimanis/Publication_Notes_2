/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.tablemodels;

import tn.manianis.entities.EleveRow;
import tn.manianis.entities.Groupe;
import tn.manianis.entities.Note;

import javax.swing.table.AbstractTableModel;
import tn.manianis.frames.MainFrame;

/**
 *
 * @author manianis
 */
public class EleveRowTableModel extends AbstractTableModel {

    private boolean editMode = true;
    private boolean editNotes = true;
    private boolean editObservations = false;
    private boolean isDirty;
    private Groupe groupe;
    private Object[][] oldData;

    public EleveRowTableModel(Groupe groupe) {
        setGroupe(groupe);
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditNotes() {
        return editNotes;
    }

    public void setEditNotes(boolean editNotes) {
        this.editNotes = editNotes;
        this.editObservations = !editNotes;
    }

    public boolean isEditObservations() {
        return editObservations;
    }

    public void setEditObservations(boolean editObservations) {
        this.editObservations = editObservations;
        this.editNotes = !editObservations;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
        isDirty = false;
        duplicateData();
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    private void duplicateData() {
        oldData = new Object[groupe.getRowCollection().size()][groupe.getEpreuves().size() + 1];
        int numEleve = 0;
        for (EleveRow row : groupe.getRowCollection()) {
            int index = 0;
            for (Note note : row.getNotes()) {
                oldData[numEleve][index] = new Note(note);
                index++;
            }
            oldData[numEleve][index] = new String(row.getObservation());
            numEleve++;
        }
    }

    private void restoreData() {
        int numEleve = 0;
        for (EleveRow row : groupe.getRowCollection()) {
            int index = 0;
            for (Note note : row.getNotes()) {
                row.getNotes()[index] = new Note((Note) oldData[numEleve][index]);
                index++;
            }
            row.setObservation((String) oldData[numEleve][index]);
            numEleve++;
        }
        fireTableDataChanged();
    }

    public boolean isDirty() {
        return isDirty;
    }

    public int columnCount() {
        return 5 + groupe.getEpreuves().size();
    }

    public Note[] calcSummary() {
        Note[] result = new Note[3];
        double count = 0;
        double sum = 0.0;
        double max = Note.NON_AFFECTEE;
        double min = Note.NON_AFFECTEE;
        for (EleveRow eleveRow : groupe.getRowCollection()) {
            Note average = eleveRow.getMoyenne();
            if (average.isValid()) {
                sum += average.getNote();
                if (max == Note.NON_AFFECTEE || average.getNote() > max) {
                    max = average.getNote();
                }
                if (max == Note.NON_AFFECTEE || average.getNote() < min) {
                    min = average.getNote();
                }
                count++;
            }
        }
        if (count > 0) {
            result[0] = new Note(sum / count);
            result[1] = new Note(max);
            result[2] = new Note(min);
        } else {
            for (int i = 0; i < result.length; i++) {
                result[i] = new Note();
            }
        }
        return result;
    }

    public boolean isNoteCell(int columnIndex) {
        return (columnIndex >= 3 && columnIndex < (3 + groupe.getEpreuves().size()));
    }

    public int getFirstNoteColumnIndex() {
        return 3;
    }

    public int getLastNoteColumnIndex() {
        return 3 + groupe.getEpreuves().size() - 1;
    }

    public void setMultipleColumns(int rowIndex, int startColumnIndex, int endColumnIndex, double value) {
        for (int i = startColumnIndex; i <= endColumnIndex; i++) {
            setValueAt(new Note(value), rowIndex, i);
        }
    }

    public Object getColumnAt(EleveRow eleveRow, int idx) {
        if (idx == 0) {
            return eleveRow.getNumOrdre();
        } else if (idx == 1) {
            return eleveRow.getIdentEleve();
        } else if (idx == 2) {
            return eleveRow.getNomEleve();
        } else if (idx >= 3 && idx < (3 + groupe.getEpreuves().size())) {
            return eleveRow.getNotes()[idx - 3];
        } else if (idx == 3 + groupe.getEpreuves().size()) {
            return eleveRow.getMoyenne();
        } else if (idx == 4 + groupe.getEpreuves().size()) {
            return eleveRow.getObservation();
        }
        return null;
    }

    public Class getColumnClassAt(int idx) {
        if (idx == 0) {
            return Integer.class;
        } else if (idx == 1) {
            return String.class;
        } else if (idx == 2) {
            return String.class;
        } else if (idx >= 3 && idx < (3 + groupe.getEpreuves().size())) {
            return Note.class;
        } else if (idx == 3 + groupe.getEpreuves().size()) {
            return Note.class;
        } else if (idx == 4 + groupe.getEpreuves().size()) {
            return String.class;
        }
        return null;
    }

    public void acceptChanges() {
        this.isDirty = false;
        fireTableDataChanged();
    }

    public void rejectChanges() {
        this.isDirty = false;
        restoreData();
    }

    public void setNote(int rowIndex, int numNote, Note note) {
        groupe.getRowCollection().setNote(rowIndex, numNote, note);
        isDirty = true;
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void setObservation(int rowIndex, String observation) {
        EleveRow elRow = groupe.getEleveRow(rowIndex);
        elRow.setObservation(observation);
        isDirty = true;
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void calcObservations() {
        for (EleveRow eleveRow : groupe.getRowCollection()) {
            eleveRow.setObservation(MainFrame.getObservations().getObservation(eleveRow.getMoyenne().getNote()));
        }
        isDirty = true;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return groupe.getRowCollection().size();
    }

    @Override
    public int getColumnCount() {
        if (!groupe.getRowCollection().isEmpty()) {
            return columnCount();
        }
        return 0;
    }

    @Override
    public String getColumnName(int column) {
        if (!groupe.getEpreuves().isEmpty()) {
            if (column == 0) {
                return "ع.ر.";
            } else if (column == 1) {
                return "المعرف";
            } else if (column == 2) {
                return "الإسم واللقب";
            } else if (column >= 3 && column < (3 + groupe.getEpreuves().size())) {
                return groupe.getEpreuve(column - 3).getAbbrNomEpreuve();
            } else if (column == 3 + groupe.getEpreuves().size()) {
                return "المعدل";
            } else if (column == 4 + groupe.getEpreuves().size()) {
                return "الملاحظة";
            }
            // groupe.getEpreuve(column).getAbbrNomEpreuve();
        }
        return super.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getColumnClassAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editMode
                && (editNotes && (columnIndex >= 3 && columnIndex < (3 + groupe.getEpreuves().size())))
                || (editObservations && (columnIndex == 4 + groupe.getEpreuves().size()));
    }

    @Override
    public Object getValueAt(int row, int col) {
        EleveRow elRow = groupe.getEleveRow(row);
        return getColumnAt(elRow, col);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex >= 3 && columnIndex < (3 + groupe.getEpreuves().size())) {
            setNote(rowIndex, columnIndex - 3, (Note) aValue);
        } else if (columnIndex == getColumnCount() - 1) {
            setObservation(rowIndex, (String) aValue);
        }
    }
}
