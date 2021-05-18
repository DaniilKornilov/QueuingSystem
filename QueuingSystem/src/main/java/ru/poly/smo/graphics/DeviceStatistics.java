package ru.poly.smo.graphics;

import java.text.DecimalFormat;

public class DeviceStatistics {
    private final int id;
    private final double useCoefficient;

    private static final DecimalFormat df = new DecimalFormat("#.###");

    public DeviceStatistics(int id, double useCoefficient) {
        this.id = id;
        this.useCoefficient = useCoefficient;
    }

    public int getId() {
        return id;
    }

    public String getUseCoefficient() {
        return df.format(useCoefficient);
    }
}
