package ru.poly.smo;

public class Main {
    static int numberOfSources = 5;
    static int numberOfRequests = 1000000;
    static double lambda = 1;
    static int numberOfDevices = 8;
    static double a = 1;
    static double b = 2;
    static int numberOfBufferElements = 3;

    public static class Statistics {
        private static final Request[] requests = new Request[numberOfRequests];
        public static int[] requestsBySources = new int[numberOfSources];
        private static final int[] refusedBySources = new int[numberOfSources];

        public static double[] pRefusal = new double[numberOfSources];

        private static final double[] fullSystemTime = new double[numberOfSources];
        public static double[] averageSystemTime = new double[numberOfSources];

        private static final double[] fullBufferTime = new double[numberOfSources];
        public static double[] averageBufferTime = new double[numberOfSources];

        private static final double[] fullProcessingTime = new double[numberOfSources];
        public static double[] averageProcessingTime = new double[numberOfSources];

        private static final double[] deviceTime = new double[numberOfDevices];
        public static double[] deviceUsingCoefficients = new double[numberOfDevices];

        public static double[] bufferDispersion = new double[numberOfSources];
        public static double[] processingDispersion = new double[numberOfSources];

        private static int currentIndex = 0;

        public static void add(Request request) {
            requests[currentIndex] = request;
            currentIndex++;
        }

        private static void countRequests() {
            for (int i = 0; i < numberOfRequests; i++) {
                int sourceId = requests[i].getSourceID();
                requestsBySources[sourceId - 1]++;
            }
        }

        private static void countPRef() {
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

        private static void countSystemTime() {
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

        private static void countBufferTime() {
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

        private static void countProcessingTime() {
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

        private static void countBufferDispersion() {
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

        private static void countProcessingDispersion() {
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

        private static void countUseCoefficient() {
            for (int i = 0; i < numberOfRequests; i++) {
                Request request = requests[i];
                int deviceId = request.getDeviceID();
                if (deviceId != -1) {
                    deviceTime[deviceId - 1] += (request.getExitTime() - request.getEntryTime());
                }
            }
            double fullTime = 0;
            for (int i = 0; i < numberOfSources; i++) {
                fullTime += fullProcessingTime[i];
            }

            for (int i = 0; i < numberOfDevices; i++) {
                deviceUsingCoefficients[i] = deviceTime[i] / fullTime;
            }
        }

        public static void collectStats() {
            countRequests();
            countPRef();
            countSystemTime();
            countBufferTime();
            countProcessingTime();
            countUseCoefficient();
            countBufferDispersion();
            countProcessingDispersion();
        }
    }

    public static void main(String[] args) {
        Controller controller = new Controller(numberOfSources, numberOfRequests, lambda, numberOfDevices, a, b, numberOfBufferElements);
        controller.generateRequests();
        Statistics.collectStats();

        System.out.println("Sources");
        System.out.println("#\t" + "Requests\t" + "Pref\t" + "T sys\t" + "T buf\t" + "T proc\t" + "Buf D\t" + "Proc D");
        for (int i = 0; i < numberOfSources; i++) {
            System.out.print(i + 1);
            System.out.print("\t");
            System.out.print(Statistics.requestsBySources[i]);
            System.out.print("\t\t");
            System.out.printf("%.3f", Statistics.pRefusal[i]);
            System.out.print("\t");
            System.out.printf("%.3f", Statistics.averageSystemTime[i]);
            System.out.print("\t");
            System.out.printf("%.3f", Statistics.averageBufferTime[i]);
            System.out.print("\t");
            System.out.printf("%.3f", Statistics.averageProcessingTime[i]);
            System.out.print("\t");
            System.out.printf("%.3f", Statistics.bufferDispersion[i]);
            System.out.print("\t");
            System.out.printf("%.3f", Statistics.processingDispersion[i]);
            System.out.println();
        }

        System.out.println("Devices");
        System.out.println("#\t" + "Use coefficient");
        for (int i = 0; i < numberOfDevices; i++) {
            System.out.print(i + 1);
            System.out.print("\t");
            System.out.printf("%.3f", Statistics.deviceUsingCoefficients[i]);
            System.out.println();
        }
    }
}
