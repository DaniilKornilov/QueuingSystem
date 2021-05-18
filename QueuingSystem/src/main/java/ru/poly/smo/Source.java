package ru.poly.smo;

public class Source {
    private final int id;
    public static int currentId = 0;
    private double lastGenTime;
    private Request lastRequest;
    private final double lambda;
    private int generatedRequests;

    public Source(double lambda) {
        this.lambda = lambda;
        id = generateId();
        lastGenTime = 0;
        lastRequest = generateNextRequest();
        generatedRequests = 0;
    }

    public Request extractLastRequest(StateHandler stateHandler) {
        Request previousRequest = lastRequest;

        //for step mode
        if (stateHandler != null && stateHandler.getCurrentNumber() < stateHandler.getNumberOfSteps()) {
            stateHandler.handleSources(new SourceState(id, lastGenTime, generatedRequests + 1));
        }
        //

        lastRequest = generateNextRequest();
        return previousRequest;
    }

    public Request getLastRequest() {
        return lastRequest;
    }

    private int generateId() {
        currentId++;
        return currentId;
    }

    public Request generateNextRequest() {
        double distribution = -1 / lambda * Math.log(Math.random());
        lastGenTime += distribution;
        lastRequest = new Request(id, lastGenTime);
        generatedRequests++;
        lastRequest.setOrder(generatedRequests);
        return lastRequest;
    }

    public int getId() {
        return id;
    }

    public int getGeneratedRequests() {
        return generatedRequests;
    }

    public double getLastGenTime() {
        return lastGenTime;
    }
}
