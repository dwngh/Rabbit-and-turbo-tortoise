package com.tortoise.ui;

import com.tortoise.component.Monitor;
import com.tortoise.network.SensorData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MonitorDashboard extends JFrame{
    private JPanel panel1;
    private JPanel sensorDataPanel;
    private int panelSize;
    private int page;
    private JButton prev;
    private JButton next;
    private JTextArea paging;
    private HashMap<Integer, SensorDataChart> charts = new HashMap<>();
    private Monitor monitor;
    private static int MAX_SIZE = 3;

    private String generatePaging() {
        return Integer.toString(panelSize) + " out of "
                + Integer.toString(charts.size()) + " (Page: " + Integer.toString(page) + ")";
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }


    private void updatePaging() {
        int start_index = this.page * MAX_SIZE;
        int end_index = start_index + MAX_SIZE > charts.size() ? charts.size() : start_index + MAX_SIZE;
        end_index--;
        sensorDataPanel.removeAll();
        sensorDataPanel.repaint();
        int index = 0;
        int item_count = 0;
        for (SensorDataChart sdc : charts.values()) {
            if (index >= start_index && index <= end_index) {
                sensorDataPanel.add(sdc);
                System.out.println(index);
                item_count++;
            }
            index++;
        }
        this.panelSize = item_count;
        sensorDataPanel.revalidate();
    }

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

        this.prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (page > 0) {
                    page--;
                    updatePaging();
                }
            }
        });

        this.next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int c = charts.size() % MAX_SIZE == 0 ? 0 : 1;
                if (page  + 1 < (int) (charts.size() / MAX_SIZE) + c ) {
                    page++;
                    updatePaging();
                }
            }
        });
    }

    public void setCompressionMode(int id, byte mode) {
        this.monitor.changeMode(id, mode);
    }

    public void process(HashMap<Integer, SensorData> data) {
        for (int sensor_id : data.keySet()) {
            SensorDataChart temp = charts.get(sensor_id);
            if (temp == null) {
                temp = new SensorDataChart(sensor_id);
                temp.addValue(data.get(sensor_id).getValue());
                charts.put(sensor_id, temp);
                if (panelSize + 1 <= MAX_SIZE) {
                    sensorDataPanel.add(temp);
                    sensorDataPanel.revalidate();
                    panel1.revalidate();
                    panelSize++;
                }
                temp.setMd(this);
                temp.setMode(data.get(sensor_id).getMode());
            } else {
                temp.addValue(data.get(sensor_id).getValue());
                temp.setMode(data.get(sensor_id).getMode());
            }
        }
        for (int sensor_id : charts.keySet()) {
            if (!data.containsKey(sensor_id)) {
                sensorDataPanel.remove(charts.get(sensor_id));
                charts.remove(sensor_id);
            }
        }
        paging.setText(generatePaging());
    }
}
