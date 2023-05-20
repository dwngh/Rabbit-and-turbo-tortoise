package com.tortoise.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EvaluationDashboard extends JFrame{
    private JPanel panel1;
    private JLabel delayValueLabel;
    private JLabel throughputValueLabel;
    private JLabel maxNode;
    private JTable table1;

    public EvaluationDashboard() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("Network Evaluation Dashboard");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.setSize(350, 260);
        this.setMinimumSize(new Dimension(350, 260));
        this.add(panel1);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void update(float delay, float throughput, int estimate_maximum_node) {
        this.delayValueLabel.setText(Float.toString(delay) + " ms");
        this.throughputValueLabel.setText(Float.toString(throughput) + " bps");
        this.maxNode.setText(Integer.toString(estimate_maximum_node) + " sensors");
    }

}
