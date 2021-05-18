package ru.poly.smo;

public class AutoModeProbabilityChecker {
    private boolean isAlright;
    private double previous;
    private double current;

    public AutoModeProbabilityChecker() {
        isAlright = false;
        previous = 0;
        current = 0;
    }

    public void setNewValue(double newValue) {
        previous = current;
        current = newValue;
        checkProbabilities();
    }

    private void checkProbabilities() {
        double state = Math.abs(current - previous) / previous;
        if (state < 0.1) {
            isAlright = true;
        }
    }

    public boolean isAlright() {
        return isAlright;
    }
}
