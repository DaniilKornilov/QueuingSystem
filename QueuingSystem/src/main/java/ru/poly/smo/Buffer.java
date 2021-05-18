package ru.poly.smo;

import java.util.LinkedList;

public class Buffer {
    private final LinkedList<Request> buffer;
    private final int maxNumberOfElements;

    public Buffer(int maxNumberOfElements) {
        this.maxNumberOfElements = maxNumberOfElements;
        buffer = new LinkedList<>();
    }

    public void addRequest(Request request, double time, StateHandler stateHandler) {
        if (buffer.size() == maxNumberOfElements) {
            replaceRequest(request, time);
            //for step mode
            if (stateHandler != null && stateHandler.getCurrentNumber() < stateHandler.getNumberOfSteps()) {
                stateHandler.handleCancelledFromBufferRequest(copyBufferState());
            }
            //
        } else {
            buffer.addFirst(request);
            //for step mode
            if (stateHandler != null && stateHandler.getCurrentNumber() < stateHandler.getNumberOfSteps()) {
                stateHandler.handleAddedToBufferRequest(copyBufferState());
            }
            //
        }
    }

    public Request extractRequestByPriority() {
        return buffer.removeFirst();
    }

    public Request getRequestByPriority() {
        return buffer.peekFirst();
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    private void replaceRequest(Request request, double time) {
        if (buffer.peekLast() != null) {
            buffer.peekLast().setCancelTime(time);
        }
        buffer.removeLast();
        buffer.addFirst(request);
    }

    private LinkedList<BufferState> copyBufferState() {
        LinkedList<BufferState> bufferStates = new LinkedList<>();
        for (int i = 0; i < buffer.size(); i++) {
            Request requestToAdd = buffer.get(i);
            double timeToAdd = requestToAdd.getGenTime();
            int order = requestToAdd.getOrder();
            int sourceNumber = requestToAdd.getSourceID();
            BufferState bufferState = new BufferState(i, timeToAdd, order, sourceNumber);
            bufferStates.add(bufferState);
        }
        return bufferStates;
    }
}
