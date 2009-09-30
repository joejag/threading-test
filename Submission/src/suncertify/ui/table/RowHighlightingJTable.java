package suncertify.ui.table;

import suncertify.ui.table.model.SimpleTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class RowHighlightingJTable extends JTable
{
  public RowHighlightingJTable(SimpleTableModel simpleTableModel)
  {
    super(simpleTableModel);

    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    this.getTableHeader().setFont(new Font("", Font.BOLD, 12));
  }

  public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
  {
    Component component = super.prepareRenderer(renderer, row, column);

    if (row % 2 == 0 && !isCellSelected(row, column))
      component.setBackground(new Color(221, 221, 221));
    else
      component.setBackground(getBackground());

    return component;
  }
}
