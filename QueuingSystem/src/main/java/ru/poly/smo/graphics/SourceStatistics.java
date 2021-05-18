package ru.poly.smo.graphics;

import java.text.DecimalFormat;

public class SourceStatistics {
    private final int number;
    private final int requestsNumber;
    private final double probabilityOfRefusal;
    private final double systemTime;
    private final double bufferTime;
    private final double processingTime;
    private final double bufferDispersion;
    private final double processingDispersion;

    private static final DecimalFormat df = new DecimalFormat("#.###");

    public SourceStatistics(int number, int requestsNumber, double probabilityOfRefusal, double systemTime, double bufferTime,
                            double processingTime, double bufferDispersion, double processingDispersion) {
        this.number = number;
        this.requestsNumber = requestsNumber;
        this.probabilityOfRefusal = probabilityOfRefusal;
        this.systemTime = systemTime;
        this.bufferTime = bufferTime;
        this.processingTime = processingTime;
        this.bufferDispersion = bufferDispersion;
        this.processingDispersion = processingDispersion;
    }

    public int getNumber() {
        return number;
    }

    public int getRequestsNumber() {
        return requestsNumber;
    }

    public String getProbabilityOfRefusal() {
        return df.format(probabilityOfRefusal);
    }

    public String getSystemTime() {
        return df.format(systemTime);
    }

    public String getBufferTime() {
        return df.format(bufferTime);
    }

    public String getProcessingTime() {
        return df.format(processingTime);
    }

    public String getBufferDispersion() {
        return df.format(bufferDispersion);
    }

    public String getProcessingDispersion() {
        return df.format(processingDispersion);
    }
}
