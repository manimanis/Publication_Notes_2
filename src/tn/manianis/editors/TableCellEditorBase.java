/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 *
 * @author manianis
 */
public class TableCellEditorBase extends DefaultCellEditor {

    protected Class[] argTypes = new Class[]{String.class};
    protected java.lang.reflect.Constructor constructor;
    protected Object value;
    protected Object oldValue;

    public TableCellEditorBase() {
        super(new JTextField());
        getComponent().setName("Table.editor");
    }

    public JTextField getTextField() {
        return (JTextField) editorComponent;
    }

    @Override
    public boolean stopCellEditing() {
        String s = (String) super.getCellEditorValue();
        try {
            if ("".equals(s)) {
                if (constructor.getDeclaringClass() == String.class) {
                    value = s;
                }
                return super.stopCellEditing();
            }

            value = constructor.newInstance(new Object[]{s});
        } catch (Exception e) {
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
            return false;
        }
        return super.stopCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected,
            int row, int column) {
        delegate.setValue(value);
        this.value = null;
        this.oldValue = value;
        ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
        try {
            Class<?> type = table.getColumnClass(column);
            // Since our obligation is to produce a value which is
            // assignable for the required type it is OK to use the
            // String constructor for columns which are declared
            // to contain Objects. A String is an Object.
            if (type == Object.class) {
                type = String.class;
            }
            constructor = type.getConstructor(argTypes);
        } catch (Exception e) {
            return null;
        }
        getTextField().selectAll();
        getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                getTextField().selectAll();
            }
        });
        return getTextField();
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }

}
