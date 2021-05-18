package ru.poly.smo.graphics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.poly.smo.AutoModeProbabilityChecker;
import ru.poly.smo.Controller;
import ru.poly.smo.Statistics;

public class AutoModeController {

    private int numberOfSources;
    private int numberOfRequests;
    private double lambda;
    private int numberOfDevices;
    private double a;
    private double b;
    private int numberOfBufferElements;
    private boolean isGeneratingNew;

    @FXML
    private TableView<SourceStatistics> sourceTable;

    @FXML
    private TableColumn<SourceStatistics, Integer> sourceNumber;

    @FXML
    private TableColumn<SourceStatistics, Integer> requestsNumber;

    @FXML
    private TableColumn<SourceStatistics, Double> refuseProbability;

    @FXML
    private TableColumn<SourceStatistics, Double> systemTime;

    @FXML
    private TableColumn<SourceStatistics, Double> bufferTime;

    @FXML
    private TableColumn<SourceStatistics, Double> processingTime;

    @FXML
    private TableColumn<SourceStatistics, Double> dBuffer;

    @FXML
    private TableColumn<SourceStatistics, Double> dProcessing;

    @FXML
    private TableView<DeviceStatistics> deviceTable;

    @FXML
    private TableColumn<DeviceStatistics, Integer> deviceNumber;

    @FXML
    private TableColumn<DeviceStatistics, Double> usingCoefficient;

    @FXML
    private Label autoMessage;

    @FXML
    void initialize() {
        numberOfSources = ValuesManager.getNumberOfSources();
        numberOfRequests = ValuesManager.getNumberOfRequests();
        lambda = ValuesManager.getLambda();
        numberOfDevices = ValuesManager.getNumberOfDevices();
        a = ValuesManager.getA();
        b = ValuesManager.getB();
        numberOfBufferElements = ValuesManager.getNumberOfBufferElements();
        isGeneratingNew = ValuesManager.isIsNew();

        sourceNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        requestsNumber.setCellValueFactory(new PropertyValueFactory<>("requestsNumber"));

        refuseProbability.setCellValueFactory(new PropertyValueFactory<>("probabilityOfRefusal"));

        systemTime.setCellValueFactory(new PropertyValueFactory<>("systemTime"));

        bufferTime.setCellValueFactory(new PropertyValueFactory<>("bufferTime"));

        processingTime.setCellValueFactory(new PropertyValueFactory<>("processingTime"));

        dBuffer.setCellValueFactory(new PropertyValueFactory<>("bufferDispersion"));

        dProcessing.setCellValueFactory(new PropertyValueFactory<>("processingDispersion"));

        deviceNumber.setCellValueFactory(new PropertyValueFactory<>("id"));

        usingCoefficient.setCellValueFactory(new PropertyValueFactory<>("useCoefficient"));

        run();
    }

    private void run() {
        Controller controller = new Controller(numberOfSources, numberOfRequests, lambda, numberOfDevices, a, b, numberOfBufferElements);
        controller.generateRequests();
        Statistics statistics = controller.getStatistics();
        statistics.collectStats();
        AutoModeProbabilityChecker checker = new AutoModeProbabilityChecker();
        checker.setNewValue(statistics.getpRefusal()[0]);
        if (isGeneratingNew) {
            while (!checker.isAlright()) {
                int newNumberOfRequests = controller.getNumberOfRequests() * 2;
                controller = new Controller(numberOfSources, newNumberOfRequests, lambda, numberOfDevices, a, b, numberOfBufferElements);
                controller.generateRequests();
                statistics = controller.getStatistics();
                statistics.collectStats();
                checker.setNewValue(statistics.getpRefusal()[0]);
                autoMessage.setText("Number of requests has been changed. New value: " + newNumberOfRequests);
            }
        }
        for (int i = 0; i < numberOfSources; i++) {
            int source = i + 1;
            int request = statistics.getRequestsBySources()[i];
            double pRef = statistics.getpRefusal()[i];
            double sysTime = statistics.getAverageSystemTime()[i];
            double bufTime = statistics.getAverageBufferTime()[i];
            double procTime = statistics.getAverageProcessingTime()[i];
            double bufD = statistics.getBufferDispersion()[i];
            double procD = statistics.getProcessingDispersion()[i];
            SourceStatistics sourceStat = new SourceStatistics(source, request, pRef, sysTime, bufTime, procTime, bufD, procD);
            sourceTable.getItems().add(sourceStat);
        }

        for (int i = 0; i < numberOfDevices; i++) {
            int device = i + 1;
            double useCoefficient = statistics.getDeviceUsingCoefficients()[i];
            DeviceStatistics deviceStat = new DeviceStatistics(device, useCoefficient);
            deviceTable.getItems().add(deviceStat);
        }
    }
}
