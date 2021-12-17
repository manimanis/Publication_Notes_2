/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.tablemodels;

import tn.manianis.entities.*;

/**
 * @author manianis
 */
public class ObservationsTableModel extends BaseTableModel<ObservationCollection> {

    public ObservationsTableModel(ObservationCollection observations) {
        super(observations);
    }

    public Class getColumnClassAt(int idx) {
        switch (idx) {
            case 0:
                return Double.class;
            case 1:
                return Double.class;
            case 2:
                return String.class;
            default:
                break;
        }
        return null;
    }

    public void resetData() {
        collection = ObservationCollection.fillDefaultValues();
        dirty = true;
        fireTableDataChanged();
    }
    
    public void removeRows(int start, int end) {
        if (start > end) {
            int aux = start;
            start = end;
            end = aux;
        }
        for (int i = end; i >= start; i--) {
            collection.remove(i);
        }
        dirty = end >= start;
        fireTableRowsDeleted(start,end);
    }

    public void addRow(Observation observation) {
        dirty = true;
        collection.add(observation);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "من";
            case 1:
                return "إلى";
            case 2:
                return "الملاحظة";
            default:
                break;
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getColumnClassAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Observation observation = collection.get(row);
        switch (column) {
            case 0:
                return observation.getNote();
            case 1:
                Double maxVal;
                if (row < collection.size() - 1) {
                    maxVal = collection.get(row + 1).getNote();
                } else {
                    maxVal = 20.0;
                }
                return maxVal;
            case 2:
                return observation.getObservation();
            default:
                break;
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        if (columnIndex >= 3 && columnIndex < (3 + groupe.getEpreuves().size())) {
//            EleveRow elRow = groupe.getEleveRow(rowIndex);
//            Double dbl = 0.0;
//            try {
//                dbl = Double.parseDouble((String) aValue);
//                if (dbl > 100) {
//                    dbl /= 100;
//                }
//                elRow.getNotes()[columnIndex - 3].setNote(dbl);
//                isDirty = true;
//                fireTableRowsUpdated(rowIndex, rowIndex);
//            } catch (NumberFormatException e) {
//                return;
//            }
//        }
    }
}
