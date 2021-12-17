/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.tablemodels;

import tn.manianis.entities.Epreuve;
import tn.manianis.entities.EpreuvesCollection;


/**
 * @author manianis
 */
public class CoefficientsTableModel extends BaseTableModel<EpreuvesCollection> {

    public CoefficientsTableModel(EpreuvesCollection epreuves) {
        super(epreuves);
    }

    public Epreuve getAtRow(int row) {
        return collection.get(row);
    }
    
    public void setCoefficient(int row, Double value) {
        getAtRow(row).setCoefficient(value);
        dirty = true;
        fireTableRowsUpdated(row, row);
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "الإمتحان";
        } else if (column == 1) {
            return "المختصر";
        } else if (column == 2) {
            return "الضارب";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else if (columnIndex == 1) {
            return String.class;
        } else if (columnIndex == 2) {
            return Double.class;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Epreuve epreuve = collection.get(row);
        if (column == 0) {
            return epreuve.getNomEpreuve();
        } else if (column == 1) {
            return epreuve.getAbbrNomEpreuve();
        } else if (column == 2) {
            return epreuve.getCoefficient();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            setCoefficient(rowIndex, (Double) aValue);
        }
    }
}
