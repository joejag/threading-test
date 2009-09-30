package suncertify.ui.tabs;

import suncertify.db.filter.SearchType;
import suncertify.service.ServiceSingleton;
import suncertify.ui.coms.Searcher;
import suncertify.ui.table.RowHighlightingJTable;
import suncertify.ui.table.ValidDateCellRenderer;
import suncertify.ui.table.model.SimpleTableModel;
import suncertify.ui.utils.PreferredColumnWidths;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchTab extends JPanel
{
  private static final String[] HEADINGS = new String[]{"Name", "Location", "Beds", "Smoking", "Price", "Date", "Customer"};

  private ServiceSingleton serviceSingleton;

  private final List<String[]> data = new ArrayList<String[]>();

  private JTable jTable;
  private JLabel statusBar;
  private JRadioButton andOption;
  private JRadioButton orOption;

  public SearchTab(ServiceSingleton serviceSingleton)
  {
    this.serviceSingleton = serviceSingleton;

    setLayout(new BorderLayout());

    add(createSearchArea(), BorderLayout.NORTH);
    add(createJTable(), BorderLayout.CENTER);

    statusBar = new JLabel();
    add(statusBar, BorderLayout.SOUTH);

    performSearch("", "");

    PreferredColumnWidths.setPreferredWidths(jTable);
  }

  private void performSearch(String nameText, String locationText)
  {
    String textualResult = new Searcher().performSearch(serviceSingleton, data, nameText, locationText, getSearchType());

    jTable.getRowSorter().modelStructureChanged();
    jTable.updateUI();
    statusBar.setText(textualResult);
  }

  private SearchType getSearchType()
  {
    SearchType type = SearchType.OR;

    if (andOption.isSelected())
    {
      type = SearchType.AND;
    } else if (orOption.isSelected())
    {
      type = SearchType.OR;
    }
    return type;
  }

  private JPanel createSearchArea()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());

    final JTextField nameField = createLabeldTextfield(panel, "Name:", 'n');
    createSearchOptionsRadio(panel);
    final JTextField locationField = createLabeldTextfield(panel, "Location:", 'l');

    nameField.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        performSearch(nameField.getText(), locationField.getText());
      }
    });
    locationField.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        performSearch(nameField.getText(), locationField.getText());
      }
    });

    JButton searchButton = new JButton("Search");
    searchButton.setMnemonic('s');
    panel.add(searchButton);
    searchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        performSearch(nameField.getText(), locationField.getText());
      }
    });

    searchButton = new JButton("Show all");
    panel.add(searchButton);
    searchButton.setMnemonic('w');
    searchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        performSearch("", "");
      }
    });

    return panel;
  }

  private void createSearchOptionsRadio(JPanel panel)
  {
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BorderLayout());
    panel.add(optionsPanel);

    andOption = new JRadioButton("AND");
    andOption.setSelected(true);
    andOption.setMnemonic('a');
    optionsPanel.add(andOption, BorderLayout.WEST);

    orOption = new JRadioButton("OR");
    orOption.setMnemonic('o');
    optionsPanel.add(orOption, BorderLayout.EAST);

    ButtonGroup group = new ButtonGroup();
    group.add(andOption);
    group.add(orOption);
  }

  private JTextField createLabeldTextfield(JPanel panel, String labelText, char accelerator)
  {
    JLabel nameLabel = new JLabel(labelText);
    nameLabel.setDisplayedMnemonic(accelerator);
    panel.add(nameLabel);

    final JTextField nameField = new JTextField(15);
    nameField.setFocusAccelerator(accelerator);
    panel.add(nameField);

    return nameField;
  }

  private JScrollPane createJTable()
  {
    SimpleTableModel tableModel = new SimpleTableModel(data, HEADINGS);
    jTable = new RowHighlightingJTable(tableModel);
    jTable.setDefaultRenderer(Object.class, new ValidDateCellRenderer());
    jTable.setAutoCreateRowSorter(true);

    jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    jTable.setColumnSelectionAllowed(false);
    jTable.setCellSelectionEnabled(false);
    jTable.setRowSelectionAllowed(true);

    return new JScrollPane(jTable);
  }
}