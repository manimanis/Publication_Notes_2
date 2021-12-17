/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author manianis
 */
public class DefaultCellRenderer extends DefaultTableCellRenderer {
    
    public static Color ODD_ROW_COLOR = new Color(227, 229, 255);
    public static Color EVEN_ROW_COLOR = new Color(255, 229, 227);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            c.setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
        }
        return c;
    }
}
