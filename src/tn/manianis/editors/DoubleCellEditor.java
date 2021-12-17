/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.editors;

import javax.swing.JOptionPane;

/**
 *
 * @author manianis
 */
public class DoubleCellEditor extends TableCellEditorBase {
    
    private double minimalValue;
    private double maximalValue;

    public DoubleCellEditor() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public DoubleCellEditor(boolean hasMinimum, double value) {
        this((hasMinimum) ? value : Double.MIN_VALUE, (hasMinimum) ? Double.MAX_VALUE : value);
    }

    public DoubleCellEditor(double minimalValue, double maximalValue) {
        this.minimalValue = minimalValue;
        this.maximalValue = maximalValue;
    }
    
    @Override
    public boolean stopCellEditing() {
        String s = (String) super.getCellEditorValue();
        try {
            double nv;
            nv = Double.parseDouble(getTextField().getText().replace(',', '.'));
            if (nv < minimalValue) {
                JOptionPane.showMessageDialog(editorComponent, "الرجاء إدخال عدد أكبر أو يساوي " + minimalValue, "خطأ قيمة غير مقبولة", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (nv > maximalValue) {
                JOptionPane.showMessageDialog(editorComponent, "الرجاء إدخال عدد أصغر أو يساوي " + maximalValue, "خطأ قيمة غير مقبولة", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            value = nv;
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(editorComponent, "لا أقبل إلا الأعداد فقط.", "خطأ قيمة غير مقبولة", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return delegate.stopCellEditing();
    }
}
