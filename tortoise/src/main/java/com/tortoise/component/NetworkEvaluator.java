package com.tortoise.component;

import com.tortoise.Tortoise;
import com.tortoise.network.SensorDataPacket;
import com.tortoise.ui.EvaluationDashboard;
import com.tortoise.util.EvaluationTable;
import com.tortoise.util.Queue;

public class NetworkEvaluator implements Runnable{
    private int sensorId = -1;
    private float s_eva;
    private long s_tm;
    private boolean prevResolved;
    private Queue<Float> delays;
    private EvaluationDashboard evaluationDashboard;
    private EvaluationTable evaluationTable;
    protected Thread t;
    private boolean runFlag;

    public boolean isRunning() {
        return runFlag;
    }

    public void terminate() {
        this.runFlag = false;
    }

    public boolean attachSensor(int id) {
        if (sensorId == -1) {
            this.sensorId = id;
            return true;
        };
        return false;
    }

    public void bind(EvaluationDashboard ed) {
        evaluationDashboard = ed;
    }

    public NetworkEvaluator() {
        prevResolved = true;
        delays = new Queue<>(10);
        evaluationTable = new EvaluationTable();
        runFlag = true;
    }

    public void updateSensor(int id, float sensorValue, long sensorTimestamp) {
        evaluationTable.updateValue(id, sensorValue, sensorTimestamp);
    }

    public void calculateSensor(int sid, float sensorValue, long monitorTimestamp, int packetSize) {
        evaluationTable.calculateValue(sid, sensorValue, monitorTimestamp, packetSize);
    }

    private float delayMean() {
        if (delays.size() == 0) return 0;
        return delays.sum() / delays.size();
    }

    public void updateCurrentSensor(float sensorValue, long sensorTimestamp) {
        if (!prevResolved) {
            System.out.println("Network problem detected!");
        }
        this.s_eva = sensorValue;
        this.s_tm = sensorTimestamp;
        prevResolved = false;
    }

    public void updateNetStatus(int sid, float sensorValue, long monitorTimestamp, int packetSize) {
        if (sid != sensorId) return;
        if (prevResolved) {
            System.out.println("Internal problem detected!");
        }
        if (this.s_eva != sensorValue) {
            System.out.println("Packet loss problem detected!");
        } else {
            long delay = monitorTimestamp - this.s_tm;
//            delays.enqueue(delay);
            float throughput = 0;
            float _dm = delayMean();
            if (_dm > 0) throughput = ((float) (packetSize * 8)) / (_dm / 1000);
            float throughput_b = throughput / 8; // Throughput in Bytes
            float single_node_throughput = 1000f / Tortoise.TIME_WINDOW * packetSize;
            int estimate_maximum_node = Math.round(throughput_b / single_node_throughput);
            if (evaluationDashboard != null) evaluationDashboard.update(delay, throughput, estimate_maximum_node);
        }
        prevResolved = true;
    }

    @Override
    public void run() {
        while (runFlag) {
            float d = evaluationTable.getAverageDelay();
            delays.enqueue(d);
            float throughput = (float) (new SensorDataPacket()).getSize() * 8f * 1000f / delayMean();
            float emn = throughput / 8 / (1000 / Tortoise.TIME_WINDOW * (new SensorDataPacket()).getSize());
            evaluationDashboard.update(d, throughput, Math.round(emn));
            try {
                Thread.sleep(Tortoise.TIME_WINDOW);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "network-evaluator");
            t.start();
        }
    }
}
