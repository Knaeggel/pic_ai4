package gui;

import code.LSTFileReader;
import code.Ram;

import javax.swing.*;
import java.awt.*;

public class CellRenderer implements ListCellRenderer {

    protected DefaultListCellRenderer defaultCell = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(index == LSTFileReader.lineNumber.get(Ram.programmCounter).intValue()) {
            defaultCell.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus).setBackground(Color.LIGHT_GRAY);
        }else{
            defaultCell.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus).setBackground(Color.WHITE);
        }
        return defaultCell;
    }

}
