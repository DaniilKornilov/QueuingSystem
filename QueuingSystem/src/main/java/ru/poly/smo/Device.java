package ru.poly.smo;

public class Device {
    private final int id;
    public static int currentId = 0;
    private double exitTime;
    private final double a;
    private final double b;

    public Device(double a, double b) {
        this.a = a;
        this.b = b;
        this.id = generateId();
        exitTime = 0;
    }

    public void processRequest(Request request, double time, StateHandler stateHandler) {
        if (time >= exitTime && request != null) {
            double distribution = a + (b - a) * Math.random();
            double entryTime = Math.max(exitTime, request.getGenTime());
            exitTime = distribution + entryTime;

            request.setEntryTime(entryTime);
            request.setExitTime(exitTime);
            request.setDeviceID(id);

            //for step mode
            if (stateHandler != null && stateHandler.getCurrentNumber() < stateHandler.getNumberOfSteps()) {
                stateHandler.handleDevices(new DeviceState(id, exitTime));
            }
            //
        }
    }

    public int getId() {
        return id;
    }

    public double getExitTime() {
        return exitTime;
    }

    private int generateId() {
        currentId++;
        return currentId;
    }
}
