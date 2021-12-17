package tn.manianis.tablemodels;

import tn.manianis.interfaces.CloneableArray;

import javax.swing.table.AbstractTableModel;

public class BaseTableModel<T extends CloneableArray> extends AbstractTableModel {

    protected boolean dirty;
    protected T collection;
    protected T collectionCopy;

    public BaseTableModel(T collection) {
        setCollection(collection);
    }

    public T getCollection() {
        return collection;
    }

    public void setCollection(T collection) {
        this.collection = collection;
        dirty = false;
        duplicateData();
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    protected void duplicateData() {
        collectionCopy = (T) collection.cloneArray();
    }
    
    protected void restoreData() {
        collection = (T) collectionCopy.cloneArray();
        fireTableDataChanged();
    }
    
    public Object getRowAt(int index) {
        return collection.get(index);
    }

    @Override
    public int getRowCount() {
        return collection.size();
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    public boolean isDirty() {
        return dirty;
    }
    
    public void acceptChanges() {
        dirty = false;
        fireTableDataChanged();
    }
    
    public void rejectChanges() {
        dirty = false;
        restoreData();
    }
}
