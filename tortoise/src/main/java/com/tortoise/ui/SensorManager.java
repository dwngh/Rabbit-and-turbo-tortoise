package com.tortoise.ui;

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
    private SensorTable sensorTable;
    private HashMap<Integer, Sensor> sensors;

    public void update(String newValue) {
        this.sensorInfo.setText(newValue);
    }

    public void bind(Sensor s) {
        sensorTable.bind(s);
    }

    public SensorManager() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("Monitor Dashboard");
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

        this.newSensor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sensor s = new Sensor();
                sensors.put(s.getId(), s);
                if (sensorTable != null) sensorTable.bind(s);
                s.start();
            }
        });
    }
}
