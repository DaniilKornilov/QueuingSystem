package ru.poly.smo;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private final List<Source> sources;

    private final int numberOfRequests;
    private final int numberOfDevices;
    private final int numberOfSources;

    private final List<Device> devices;

    private final Buffer buffer;

    private double currentTime;

    private StateHandler stateHandler;
    private Statistics statistics;

    public Controller(int numberOfSources, int numberOfRequests, double lambda, int numberOfDevices, double a, double b, int numberOfBufferElements) {
        sources = new LinkedList<>();
        for (int i = 0; i < numberOfSources; i++) {
            sources.add(new Source(lambda));
        }
        this.numberOfSources = numberOfSources;

        this.numberOfRequests = numberOfRequests;

        devices = new LinkedList<>();
        for (int i = 0; i < numberOfDevices; i++) {
            devices.add(new Device(a, b));
        }

        this.numberOfDevices = numberOfDevices;

        buffer = new Buffer(numberOfBufferElements);
    }

    //for step mode
    public Controller(int numberOfSources, int numberOfRequests, double lambda, int numberOfDevices, double a, double b, int numberOfBufferElements, int numberOfSteps) {
        this(numberOfSources, numberOfRequests, lambda, numberOfDevices, a, b, numberOfBufferElements);
        stateHandler = new StateHandler(numberOfSteps, numberOfSources, numberOfBufferElements, numberOfDevices);
    }
    //

    public void generateRequests() {
        //new Stats
        statistics = new Statistics(this);
        //
        for (int i = 0; i < numberOfRequests; i++) {
            addRequest(Collections.min(sources,
                    Comparator.comparingDouble(src -> src.getLastRequest().getGenTime()))
                    .extractLastRequest(stateHandler));
        }
        //restockIDS
        Device.currentId = 0;
        Source.currentId = 0;
        //
    }

    public StateHandler getStateHandler() {
        return stateHandler;
    }

    private void addRequest(Request request) {
        //Main.Statistics.add(request);
        statistics.add(request);

        currentTime = request.getGenTime();

        updateSystem(currentTime);

        Device requestDevice = devices.stream()
                .filter(device -> device.getExitTime() < currentTime)
                .min(Comparator.comparingInt(Device::getId)).orElse(null);

        if (buffer.isEmpty() && requestDevice != null) {
            requestDevice.processRequest(request, currentTime, stateHandler);
        } else {
            buffer.addRequest(request, currentTime, stateHandler);
        }
    }

    private void updateSystem(double currentTime) {

        Request bufferRequest = buffer.getRequestByPriority();
        Device requestDevice = getFreeDeviceByPriority(currentTime);

        while ((requestDevice != null) && (bufferRequest != null)) {
            requestDevice.processRequest(buffer.extractRequestByPriority(), currentTime, stateHandler);
            requestDevice = getFreeDeviceByPriority(currentTime);
            bufferRequest = buffer.getRequestByPriority();
        }
    }

    private Device getFreeDeviceByPriority(double currentTime) {
        return devices.stream()
                .filter(device -> device.getExitTime() <= currentTime)
                .min(Comparator.comparingDouble(Device::getExitTime)).orElse(null);
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public int getNumberOfDevices() {
        return numberOfDevices;
    }

    public int getNumberOfSources() {
        return numberOfSources;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
