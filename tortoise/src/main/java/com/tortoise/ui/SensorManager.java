package com.tortoise.ui;

import com.tortoise.component.Sensor;

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
    private HashMap<Integer, Sensor> sensors;

    public void update(String newValue) {
        this.sensorInfo.setText(newValue);
    }


    public SensorManager() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("Monitor Dashboard");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.setSize(700, 560);
        this.setMinimumSize(new Dimension(500, 350));
        this.add(panel1);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.sensors = new HashMap<>();
    }
}
