package ru.poly.smo.graphics;

public class ValuesManager {
    private static int numberOfSources;
    private static int numberOfRequests;
    private static double lambda;
    private static int numberOfDevices;
    private static double a;
    private static double b;
    private static int numberOfBufferElements;
    private static int numberOfSteps;
    private static boolean isNew;

    public static int getNumberOfSources() {
        return numberOfSources;
    }

    public static void setNumberOfSources(int numberOfSources) {
        ValuesManager.numberOfSources = numberOfSources;
    }

    public static int getNumberOfRequests() {
        return numberOfRequests;
    }

    public static void setNumberOfRequests(int numberOfRequests) {
        ValuesManager.numberOfRequests = numberOfRequests;
    }

    public static double getLambda() {
        return lambda;
    }

    public static void setLambda(double lambda) {
        ValuesManager.lambda = lambda;
    }

    public static int getNumberOfDevices() {
        return numberOfDevices;
    }

    public static void setNumberOfDevices(int numberOfDevices) {
        ValuesManager.numberOfDevices = numberOfDevices;
    }

    public static double getA() {
        return a;
    }

    public static void setA(double a) {
        ValuesManager.a = a;
    }

    public static double getB() {
        return b;
    }

    public static void setB(double b) {
        ValuesManager.b = b;
    }

    public static int getNumberOfBufferElements() {
        return numberOfBufferElements;
    }

    public static void setNumberOfBufferElements(int numberOfBufferElements) {
        ValuesManager.numberOfBufferElements = numberOfBufferElements;
    }

    public static int getNumberOfSteps() {
        return numberOfSteps;
    }

    public static void setNumberOfSteps(int numberOfSteps) {
        ValuesManager.numberOfSteps = numberOfSteps;
    }

    public static boolean isIsNew() {
        return isNew;
    }

    public static void setIsNew(boolean isNew) {
        ValuesManager.isNew = isNew;
    }
}
