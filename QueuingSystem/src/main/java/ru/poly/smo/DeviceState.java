package ru.poly.smo;

import java.text.DecimalFormat;

public class DeviceState {
    private final int deviceNumber;
    private final double time;

    private static final DecimalFormat df = new DecimalFormat("#.###");

    public DeviceState(int deviceNumber, double time) {
        this.deviceNumber = deviceNumber;
        this.time = time;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public String getTime() {
        return df.format(time);
    }
}
