package tn.manianis.tablemodels;

import tn.manianis.entities.Epreuve;
import tn.manianis.entities.EpreuvesCollection;
import tn.manianis.entities.entities.secondary.XmlFileEntry;
import tn.manianis.entities.entities.secondary.XmlFileEntryCollection;

/**
 * @author manianis
 */
public class XmlFilesEntriesModel extends BaseTableModel<XmlFileEntryCollection> {
    
    public XmlFilesEntriesModel(XmlFileEntryCollection xmlFiles) {
        super(xmlFiles);
    }
    
    public XmlFileEntry getAtRow(int row) {
        return collection.get(row);
    }
    
    @Override
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "إختيار";
            case 1:
                return "الملف";
            case 2:
                return "المكان";
            default:
                break;
        }
        return "";
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                break;
        }
        return null;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        XmlFileEntry fileEntry = collection.get(row);
        switch (column) {
            case 0:
                return fileEntry.getChecked();
            case 1:
                return fileEntry.getFilename();
            case 2:
                return fileEntry.getFilepath();
            default:
                break;
        }
        return "";
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            XmlFileEntry fileEntry = collection.get(rowIndex);
            fileEntry.setChecked((Boolean) aValue);
        }
    }
}
