package com.tortoise.ui;

import com.tortoise.component.NetworkEvaluator;
import com.tortoise.component.Sensor;
import com.tortoise.util.SensorTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorManager extends JFrame{
    private JPanel panel1;
    private JButton newSensor;
    private JButton deleteSensor;
    private JTextPane sensorInfo;
    private JButton nextPageButton;
    private JButton previousPageButton;
    private JLabel paging;
    private SensorTable sensorTable;
    private HashMap<Integer, Sensor> sensors;
    private NetworkEvaluator networkEvaluator;
    private int page;
    private String original_text;

    private void setPageLabel() {
        int total_page;
        total_page = original_text == null ? 0 : original_text.length() / 954;
        paging.setText("Page " + Integer.toString(page) + " of " + Integer.toString(total_page));
    }

    private String getTrimmedText() {
        int current_len = original_text.length() / 954;
        if (current_len == 0) return original_text;
        if (page <= current_len) {
            int start_index = page * 954;
            int end_index = start_index + 954;
            end_index = end_index > original_text.length() ? original_text.length() : end_index;
            return original_text.substring(start_index, end_index);
        } else {
            return "";
        }
    }

    public void update(String newValue) {
        this.original_text = newValue;
        this.sensorInfo.setText(getTrimmedText());
        setPageLabel();
    }

    public void bind(Sensor s) {
        sensorTable.bind(s);
    }

    public SensorManager() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("Sensor Manager");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.setSize(700, 560);
        this.setMinimumSize(new Dimension(500, 350));
        deleteSensor.setVisible(false);
        this.add(panel1);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.sensorTable = new SensorTable(this);
        this.sensors = new HashMap<>();
        sensorTable.start();
        this.page = 0;
        setPageLabel();

        this.newSensor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sensor s = new Sensor(networkEvaluator);
                sensors.put(s.getId(), s);
                if (sensorTable != null) sensorTable.bind(s);
                s.start();
            }
        });

        this.nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (page < original_text.length() / 954) page++;
                setPageLabel();
            }
        });

        this.previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (page > 0) page--;
                setPageLabel();
            }
        });
    }

    public void setNetworkEvaluator(NetworkEvaluator networkEvaluator) {
        this.networkEvaluator = networkEvaluator;
    }
}
