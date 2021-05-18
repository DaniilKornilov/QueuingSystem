package ru.poly.smo;

import java.util.LinkedList;

public class State {
    private final LinkedList<SourceState> sources;
    private final LinkedList<BufferState> buffer;
    private final LinkedList<DeviceState> devices;
    private String message;

    public State(LinkedList<SourceState> sources, LinkedList<BufferState> buffer, LinkedList<DeviceState> devices, String message) {
        this.sources = sources;
        this.buffer = buffer;
        this.devices = devices;
        this.message = message;
    }

    public LinkedList<SourceState> getSources() {
        return sources;
    }

    public LinkedList<BufferState> getBuffer() {
        return buffer;
    }

    public LinkedList<DeviceState> getDevices() {
        return devices;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
