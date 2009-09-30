package suncertify.ui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ValidDateCellRenderer extends DefaultTableCellRenderer
{
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    if (table.getColumnModel().getColumn(column).getModelIndex() == 5)
    {
      Font f = new Font("", Font.BOLD, 12);
      component.setFont(f);
      component.setForeground(Color.RED);
    } else
    {
      component.setFont(new Font("", Font.PLAIN, 12));
      component.setForeground(Color.BLACK);
    }

    return component;
  }
}
