package ru.poly.smo;

import java.util.LinkedList;

public class StateHandler {
    private final int numberOfSteps;
    private int currentNumber;
    private final LinkedList<State> states;

    public StateHandler(int numberOfSteps, int numberOfSources, int numberOfBufferElements, int numberOfDevices) {
        this.numberOfSteps = numberOfSteps;
        currentNumber = 0;

        LinkedList<SourceState> sources = new LinkedList<>();
        for (int i = 0; i < numberOfSources; i++) {
            SourceState source = new SourceState(i + 1, 0, 0);
            sources.add(source);
        }
        LinkedList<DeviceState> devices = new LinkedList<>();
        for (int i = 0; i < numberOfDevices; i++) {
            DeviceState device = new DeviceState(i + 1, 0);
            devices.add(device);
        }
        LinkedList<BufferState> buffer = new LinkedList<>();
        for (int i = 0; i < numberOfBufferElements; i++) {
            BufferState bufferPlaceState = new BufferState(i + 1, 0, 0, 0);
            buffer.add(bufferPlaceState);
        }

        State state = new State(sources, buffer, devices, "Initial");
        states = new LinkedList<>();
        states.add(state);
        currentNumber++;
    }

    public void handleSources(SourceState sourceState) {
        State lastState = getLastState();
        LinkedList<SourceState> sourceStates = new LinkedList<>(lastState.getSources());
        sourceStates.set(sourceState.getSourceNumber() - 1, sourceState);
        State newState = new State(sourceStates, lastState.getBuffer(), lastState.getDevices(), lastState.getMessage());
        newState.setMessage("Request generated");
        addState(newState);
    }

    public void handleDevices(DeviceState device) {
        State lastState = getLastState();
        LinkedList<DeviceState> deviceStates = new LinkedList<>(lastState.getDevices());
        deviceStates.set(device.getDeviceNumber() - 1, device);
        State newState = new State(lastState.getSources(), lastState.getBuffer(), deviceStates, lastState.getMessage());
        newState.setMessage("Request processed");
        addState(newState);
    }

    public void handleAddedToBufferRequest(LinkedList<BufferState> bufferStates) {
        State lastState = getLastState();
        State newState = new State(lastState.getSources(), bufferStates, lastState.getDevices(), lastState.getMessage());
        newState.setMessage("Added to buffer");
        addState(newState);
    }

    public void handleCancelledFromBufferRequest(LinkedList<BufferState> bufferStates) {
        State lastState = getLastState();
        State newState = new State(lastState.getSources(), bufferStates, lastState.getDevices(), lastState.getMessage());
        newState.setMessage("Cancelled from buffer");
        addState(newState);
    }

    public LinkedList<State> getStates() {
        return states;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    private State getLastState() {
        return states.peekLast();
    }

    private void addState(State state) {
        if (currentNumber == numberOfSteps) {
            return;
        }
        states.add(state);
        currentNumber++;
    }
}
