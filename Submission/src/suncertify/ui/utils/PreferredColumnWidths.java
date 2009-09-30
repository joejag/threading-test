package suncertify.ui.utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class PreferredColumnWidths
{
  public static void setPreferredWidths(JTable jTable)
  {
    TableColumnModel columnModel = jTable.getColumnModel();
    TableModel model = jTable.getModel();

    for (int col = 0; col < columnModel.getColumnCount(); col++)
    {
      TableColumn tableColumn = columnModel.getColumn(col);

      int headerSize = getHeaderSize(jTable, col, tableColumn.getHeaderValue());
      int cellSize = getCellSize(jTable, model, col, tableColumn.getModelIndex());
      int width = Math.max(headerSize, cellSize);

      if (width >= 0)
        tableColumn.setPreferredWidth(width + columnModel.getColumnMargin());
    }
  }

  private static int getHeaderSize(JTable jTable, int col, Object value)
  {
    TableCellRenderer renderer = jTable.getTableHeader().getDefaultRenderer();
    return getWidthFromRenderer(jTable, renderer, 0, col, value);
  }

  private static int getCellSize(JTable jTable, TableModel data, int col, int columnIndex)
  {
    int width = -1;

    for (int row = 0; row < data.getRowCount(); row++)
    {
      TableCellRenderer renderer = jTable.getCellRenderer(row, col);
      width = Math.max(width, getWidthFromRenderer(jTable, renderer, row, col, data.getValueAt(row, columnIndex)));
    }

    return width;
  }

  private static int getWidthFromRenderer(JTable jTable, TableCellRenderer renderer, int row, int col, Object value)
  {
    Component component = renderer.getTableCellRendererComponent(jTable, value, false, false, row, col);
    return component.getPreferredSize().width;
  }
}
