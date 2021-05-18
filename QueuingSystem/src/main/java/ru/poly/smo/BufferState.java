package ru.poly.smo;

import java.text.DecimalFormat;

public class BufferState {
    private final int bufferPlace;
    private final double time;
    private final int requestNumber;
    private final int sourceNumber;

    private static final DecimalFormat df = new DecimalFormat("#.###");

    public BufferState(int bufferPlace, double time, int requestNumber, int sourceNumber) {
        this.bufferPlace = bufferPlace;
        this.time = time;
        this.requestNumber = requestNumber;
        this.sourceNumber = sourceNumber;
    }

    public int getBufferPlace() {
        return bufferPlace;
    }

    public String getTime() {
        return df.format(time);
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }
}
