package com.tortoise.ui;

import com.tortoise.component.Monitor;

import javax.swing.*;
import java.awt.*;

public class MonitorDashboard extends JFrame{
    private JPanel panel1;
    private JPanel sensorDataPanel;
    private int panelSize;
    private int page;
    private JButton prev;
    private JButton next;
    private JTextArea paging;
    private Monitor monitor;
    private static int MAX_SIZE = 3;


    public MonitorDashboard() throws Exception{
        super("Monitor Dashboard");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        sensorDataPanel.setLayout(new GridLayout(3,1));
        this.setSize(1000, 700);
        this.setMinimumSize(new Dimension(1200, 750));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel1);
        this.sensorDataPanel.setMinimumSize(new Dimension(1200, 500));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.panelSize = 0;
        this.page = 0;
    }
}
