package suncertify.ui.table.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SimpleTableModel extends AbstractTableModel
{
  private List<String[]> data;
  private String[] headings;

  public SimpleTableModel(List<String[]> data, String[] headings)
  {
    this.data = data;
    this.headings = headings;
  }

  public int getRowCount()
  {
    return data.size();
  }

  public int getColumnCount()
  {
    return headings.length;
  }

  public Class<?> getColumnClass(int columnIndex)
  {
    if (columnIndex == 3)
      return Boolean.class;

    return super.getColumnClass(columnIndex);
  }

  public String getColumnName(int column)
  {
    return headings[column];
  }

  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return false;
  }

  public Object getValueAt(int row, int column)
  {
    String value = data.get(row)[column];

    if (column == 3)
      return value.equalsIgnoreCase("Y");

    return value;
  }

  public void setValueAt(Object o, int row, int column)
  {
    if (column == 3)
    {
      data.get(row)[column] = data.get(row)[column].equals("Y") ? "N" : "Y";
    } else
    {
      data.get(row)[column] = o.toString();
    }
  }
}
