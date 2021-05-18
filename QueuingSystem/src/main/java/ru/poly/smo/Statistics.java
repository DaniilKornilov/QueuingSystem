package ru.poly.smo;

public class Statistics {
    private Request[] requests;
    private int[] requestsBySources;
    private int[] refusedBySources;

    private double[] pRefusal;

    private double[] averageSystemTime;

    private double[] averageBufferTime;

    private double[] averageProcessingTime;

    private double[] deviceUsingCoefficients;

    private double[] bufferDispersion;

    private double[] processingDispersion;

    private int currentIndex;
    private double fullSystemTimeForAll;

    private final int numberOfRequests;
    private final int numberOfSources;
    private final int numberOfDevices;

    public Statistics(Controller controller) {
        this.numberOfDevices = controller.getNumberOfDevices();
        this.numberOfRequests = controller.getNumberOfRequests();
        this.numberOfSources = controller.getNumberOfSources();
        init();
    }

    private void init() {
        currentIndex = 0;
        requests = new Request[numberOfRequests];
        requestsBySources = new int[numberOfSources];
        refusedBySources = new int[numberOfSources];
        pRefusal = new double[numberOfSources];
        averageSystemTime = new double[numberOfSources];
        averageBufferTime = new double[numberOfSources];
        averageProcessingTime = new double[numberOfSources];
        deviceUsingCoefficients = new double[numberOfDevices];
        bufferDispersion = new double[numberOfSources];
        processingDispersion = new double[numberOfSources];
    }

    public void add(Request request) {
        requests[currentIndex] = request;
        currentIndex++;
    }

    private void findFullSystemTime() {
        double fullTime = 0;
        for (int i = 0; i < numberOfRequests; i++) {
            fullTime = Math.max(fullTime, requests[i].getExitTime());
        }
        fullSystemTimeForAll = fullTime;
    }

    private void countRequests() {
        for (int i = 0; i < numberOfRequests; i++) {
            int sourceId = requests[i].getSourceID();
            requestsBySources[sourceId - 1]++;
        }
    }

    private void countPRef() {
        for (int i = 0; i < numberOfRequests; i++) {
            int sourceId = requests[i].getSourceID();
            int deviceId = requests[i].getDeviceID();
            if (deviceId == -1) {
                refusedBySources[sourceId - 1]++;
            }
        }
        for (int i = 0; i < numberOfSources; i++) {
            pRefusal[i] = (double) refusedBySources[i] / requestsBySources[i];
        }
    }

    private void countSystemTime() {
        double[] fullSystemTime = new double[numberOfSources];
        for (int i = 0; i < numberOfRequests; i++) {
            Request request = requests[i];
            int sourceId = request.getSourceID();
            int deviceId = request.getDeviceID();
            double cancelTime = request.getCancelTime();
            double systemTime = 0;
            if (deviceId != -1) {
                systemTime = request.getExitTime() - request.getGenTime();
            } else if (cancelTime > 0) {
                systemTime = request.getCancelTime() - request.getGenTime();
            }
            fullSystemTime[sourceId - 1] += systemTime;
        }
        for (int i = 0; i < numberOfSources; i++) {
            averageSystemTime[i] = fullSystemTime[i] / requestsBySources[i];
        }
    }

    private void countBufferTime() {
        double[] fullBufferTime = new double[numberOfSources];
        for (int i = 0; i < numberOfRequests; i++) {
            Request request = requests[i];
            int sourceId = request.getSourceID();
            int deviceId = request.getDeviceID();
            double cancelTime = request.getCancelTime();
            double bufferTime = 0;
            if (deviceId != -1) {
                bufferTime = request.getEntryTime() - request.getGenTime();
            } else if (cancelTime > 0) {
                bufferTime = request.getCancelTime() - request.getGenTime();
            }
            fullBufferTime[sourceId - 1] += bufferTime;
        }
        for (int i = 0; i < numberOfSources; i++) {
            averageBufferTime[i] = fullBufferTime[i] / requestsBySources[i];
        }
    }

    private void countProcessingTime() {
        double[] fullProcessingTime = new double[numberOfSources];
        for (int i = 0; i < numberOfRequests; i++) {
            Request request = requests[i];
            int sourceId = request.getSourceID();
            int deviceId = request.getDeviceID();
            double cancelTime = request.getCancelTime();
            double processingTime = 0;
            if (deviceId != -1) {
                processingTime = request.getExitTime() - request.getEntryTime();
            } else if (cancelTime > 0) {
                processingTime = 0;
            }
            fullProcessingTime[sourceId - 1] += processingTime;
        }
        for (int i = 0; i < numberOfSources; i++) {
            averageProcessingTime[i] = fullProcessingTime[i] / requestsBySources[i];
        }
    }

    private void countBufferDispersion() {
        for (int i = 0; i < numberOfRequests; i++) {
            Request request = requests[i];
            int sourceId = request.getSourceID();
            int deviceId = request.getDeviceID();
            double cancelTime = request.getCancelTime();
            double bufferTime = 0;
            if (deviceId != -1) {
                bufferTime = request.getEntryTime() - request.getGenTime();
            } else if (cancelTime > 0) {
                bufferTime = request.getCancelTime() - request.getGenTime();
            }
            bufferDispersion[sourceId - 1] += Math.pow(averageBufferTime[sourceId - 1] - bufferTime, 2);
        }
        for (int i = 0; i < numberOfSources; i++) {
            bufferDispersion[i] /= requestsBySources[i];
        }
    }

    private void countProcessingDispersion() {
        for (int i = 0; i < numberOfRequests; i++) {
            Request request = requests[i];
            int sourceId = request.getSourceID();
            int deviceId = request.getDeviceID();
            double cancelTime = request.getCancelTime();
            double processingTime = 0;
            if (deviceId != -1) {
                processingTime = request.getExitTime() - request.getEntryTime();
            } else if (cancelTime > 0) {
                processingTime = 0;
            }
            processingDispersion[sourceId - 1] += Math.pow(averageProcessingTime[sourceId - 1] - processingTime, 2);
        }
        for (int i = 0; i < numberOfSources; i++) {
            processingDispersion[i] /= requestsBySources[i];
        }
    }

    private void countUseCoefficient() {
        for (int i = 0; i < numberOfRequests; i++) {
            Request request = requests[i];
            int deviceId = request.getDeviceID();
            if (deviceId != -1) {
                deviceUsingCoefficients[deviceId - 1] += (request.getExitTime() - request.getEntryTime());
            }
        }
        for (int i = 0; i < numberOfDevices; i++) {
            deviceUsingCoefficients[i] /= fullSystemTimeForAll;
        }
    }

    public void collectStats() {
        findFullSystemTime();
        countRequests();
        countPRef();
        countSystemTime();
        countBufferTime();
        countProcessingTime();
        countUseCoefficient();
        countBufferDispersion();
        countProcessingDispersion();
    }

    public int[] getRequestsBySources() {
        return requestsBySources;
    }

    public double[] getpRefusal() {
        return pRefusal;
    }

    public double[] getAverageSystemTime() {
        return averageSystemTime;
    }

    public double[] getAverageBufferTime() {
        return averageBufferTime;
    }

    public double[] getAverageProcessingTime() {
        return averageProcessingTime;
    }

    public double[] getDeviceUsingCoefficients() {
        return deviceUsingCoefficients;
    }

    public double[] getBufferDispersion() {
        return bufferDispersion;
    }

    public double[] getProcessingDispersion() {
        return processingDispersion;
    }

    public double getSystemTimeStage4() {
        double time = 0;
        for (int i = 0; i < numberOfSources; i++) {
            time += averageSystemTime[i];
        }
        return time / numberOfSources;
    }

    public double getpRefusalStage4() {
        double prob = 0;
        for (int i = 0; i < numberOfSources; i++) {
            prob += pRefusal[i];
        }
        return prob / numberOfSources;
    }

    public double getSystemLoadStage4() {
        double load = 0;
        for (int i = 0; i < numberOfDevices; i++) {
            load += deviceUsingCoefficients[i];
        }
        return load / numberOfDevices;
    }
}
