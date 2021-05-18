package ru.poly.smo;

import java.text.DecimalFormat;

public class SourceState {
    private final int sourceNumber;
    private final double time;
    private final int numberOfRequests;

    private static final DecimalFormat df = new DecimalFormat("#.###");

    public SourceState(int sourceNumber, double time, int numberOfRequests) {
        this.sourceNumber = sourceNumber;
        this.time = time;
        this.numberOfRequests = numberOfRequests;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public String getTime() {
        return df.format(time);
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }
}
