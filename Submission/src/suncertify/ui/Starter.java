package suncertify.ui;

import suncertify.service.ServiceSingleton;
import suncertify.ui.tabs.SearchTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Starter extends JFrame
{
  private static final String APPLICATION_NAME = "URLyBird 1.1.1: Booking System";
  
  private final ServiceSingleton serviceSingleton;

  public static void main(String[] args)
  {
    new Starter();
  }

  public Starter()
  {
    serviceSingleton = ServiceSingleton.getInstance();

    JTabbedPane jTabbedPane = new JTabbedPane();
    jTabbedPane.add("1: Search", new SearchTab(serviceSingleton));
    jTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    jTabbedPane.add("2: Create", new JPanel());
    jTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

    add(jTabbedPane);

    postCreate();
  }

  private void postCreate()
  {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    this.setSize(800, 500);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);

    setVisible(true);
    this.setTitle(APPLICATION_NAME);
  }

}
