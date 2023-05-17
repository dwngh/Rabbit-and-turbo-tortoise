package com.tortoise.ui;

import com.tortoise.component.Sensor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SensorDataChart extends JPanel {
    /** The time series data. */
    private TimeSeries series;
    private JTextField modeString;
    private JPanel buttonPanel;
    private boolean isEvaluating;
    private JButton button;
    private JButton button1;

    private void setModeString() {
        if (mode == Sensor.ENA_COMPRESSION) {
            modeString.setText("Mode: COMPRESS");
        } else if (mode == Sensor.NO_COMPRESSION) {
            modeString.setText("Mode: NO_COMPRESS");
        } else if (mode == Sensor.EVALUATION) {
            modeString.setText("Mode: EVALUATION                     ");
            buttonPanel.remove(button);
            buttonPanel.remove(button1);
            buttonPanel.revalidate();
        }
    }

    /** The most recent value added. */
    private double lastValue = 100.0;
    private MonitorDashboard md;
    private byte mode;

    public void setMd(MonitorDashboard md) {
        this.md = md;
    }

    public void setMode(byte mode) {
        this.mode = mode;
        setModeString();
    }

    private int id;

    public SensorDataChart(int id) {
        super(new BorderLayout());
        this.id = id;
        this.series = new TimeSeries("Sensor value", Millisecond.class);

        button = new JButton("Switch compression mode off");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                md.setCompressionMode(id, Sensor.NO_COMPRESSION);
            }
        });
        button1 = new JButton("Switch compression mode on");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                md.setCompressionMode(id, Sensor.ENA_COMPRESSION);
            }
        });
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset, "Sensor-" + Integer.toString(id));
        final ChartPanel chartPanel = new ChartPanel(chart);
        buttonPanel = new JPanel(new GridLayout(3, 1));
        this.setMaximumSize(new Dimension(250, 50));
        this.modeString = new JTextField();
        this.modeString.setEditable(false);

        this.add(chartPanel);
        buttonPanel.add(modeString);
        buttonPanel.add(button);
        buttonPanel.add(button1);
        this.add(buttonPanel, BorderLayout.EAST);
        this.setVisible(true);
    }

    private JFreeChart createChart(final XYDataset dataset, String title) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                title,
                "Time",
                "Value",
                dataset,
                false,
                false,
                false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 20);
        return result;
    }

    public void addValue(float value) {
        this.series.add(new Millisecond(), value);
    }
}
