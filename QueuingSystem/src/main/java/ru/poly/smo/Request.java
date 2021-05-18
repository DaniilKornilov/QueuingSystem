package ru.poly.smo;

public class Request {
    private final int sourceID;
    private int deviceID = -1;
    private final double genTime;
    private double exitTime;
    private double entryTime;
    private double cancelTime;
    private int order;

    public Request(int sourceID, double genTime) {
        this.sourceID = sourceID;
        this.genTime = genTime;
    }

    public void setExitTime(double exitTime) {
        this.exitTime = exitTime;
    }

    public void setEntryTime(double entryTime) {
        this.entryTime = entryTime;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public void setCancelTime(double cancelTime) {
        this.cancelTime = cancelTime;
    }

    public double getExitTime() {
        return exitTime;
    }

    public double getEntryTime() {
        return entryTime;
    }

    public int getSourceID() {
        return sourceID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public double getGenTime() {
        return genTime;
    }

    public double getCancelTime() {
        return cancelTime;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Request{" +
                "sourceID=" + sourceID +
                ", deviceID=" + deviceID +
                ", genTime=" + genTime +
                ", exitTime=" + exitTime +
                ", entryTime=" + entryTime +
                ", cancelTime=" + cancelTime +
                '}';
    }
}
