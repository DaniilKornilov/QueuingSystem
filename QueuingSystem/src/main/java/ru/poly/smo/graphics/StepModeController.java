package ru.poly.smo.graphics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.poly.smo.*;

import java.util.LinkedList;

public class StepModeController {

    private int numberOfSources;
    private int numberOfRequests;
    private double lambda;
    private int numberOfDevices;
    private double a;
    private double b;
    private int numberOfBufferElements;
    private int numberOfSteps;

    @FXML
    private TableView<SourceState> sourceStateTable;

    @FXML
    private TableColumn<SourceState, Integer> sourceN;

    @FXML
    private TableColumn<SourceState, Double> sourceTime;

    @FXML
    private TableColumn<SourceState, Integer> sourceNumberOfRequests;

    @FXML
    private TableView<DeviceState> deviceStateTable;

    @FXML
    private TableColumn<DeviceState, Integer> deviceNumber;

    @FXML
    private TableColumn<DeviceState, Double> deviceTime;

    @FXML
    private TableView<BufferState> bufferStateTable;

    @FXML
    private TableColumn<BufferState, Integer> bufferPlace;

    @FXML
    private TableColumn<BufferState, Double> bufferTime;

    @FXML
    private TableColumn<BufferState, Integer> sourceNumberInBuffer;

    @FXML
    private TableColumn<BufferState, Integer> requestNumberInBuffer;

    @FXML
    private Label message;

    @FXML
    private Label stepNumber;

    @FXML
    private Button next;

    @FXML
    private Button previous;

    private LinkedList<State> states;

    private int currentStep = 0;

    @FXML
    void initialize() {
        numberOfSources = ValuesManager.getNumberOfSources();
        numberOfRequests = ValuesManager.getNumberOfRequests();
        lambda = ValuesManager.getLambda();
        numberOfDevices = ValuesManager.getNumberOfDevices();
        a = ValuesManager.getA();
        b = ValuesManager.getB();
        numberOfBufferElements = ValuesManager.getNumberOfBufferElements();
        numberOfSteps = ValuesManager.getNumberOfSteps();

        sourceN.setCellValueFactory(new PropertyValueFactory<>("sourceNumber"));

        sourceTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        sourceNumberOfRequests.setCellValueFactory(new PropertyValueFactory<>("numberOfRequests"));

        deviceNumber.setCellValueFactory(new PropertyValueFactory<>("deviceNumber"));

        deviceTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        bufferPlace.setCellValueFactory(new PropertyValueFactory<>("bufferPlace"));

        bufferTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        sourceNumberInBuffer.setCellValueFactory(new PropertyValueFactory<>("requestNumber"));

        requestNumberInBuffer.setCellValueFactory(new PropertyValueFactory<>("sourceNumber"));

        run();
    }

    private void run() {
        Controller controller = new Controller(numberOfSources, numberOfRequests, lambda, numberOfDevices, a, b, numberOfBufferElements, numberOfSteps);
        controller.generateRequests();
        StateHandler handler = controller.getStateHandler();
        states = handler.getStates();
        initState();

        next.setOnAction(event -> {
            if (currentStep < numberOfSteps - 1 && currentStep < states.size()) currentStep++;
            else return;
            sourceStateTable.getItems().clear();
            bufferStateTable.getItems().clear();
            deviceStateTable.getItems().clear();
            newState(currentStep);
        });

        previous.setOnAction(event -> {
            if (currentStep > 0) currentStep--;
            else return;
            sourceStateTable.getItems().clear();
            bufferStateTable.getItems().clear();
            deviceStateTable.getItems().clear();
            newState(currentStep);
        });
    }

    private void initState() {
        State initState = states.get(0);
        message.setText(initState.getMessage());
        stepNumber.setText(String.valueOf(1));
        updateState(initState);
    }

    private void newState(int index) {
        if (index < numberOfSteps && index >= 0) {
            State newState = states.get(index);
            message.setText(newState.getMessage());
            stepNumber.setText(String.valueOf(index + 1));
            updateState(newState);
        }
    }

    private void updateState(State newState) {
        LinkedList<SourceState> sources = newState.getSources();
        for (int i = 0; i < numberOfSources; i++) {
            SourceState sourceState = sources.get(i);
            sourceStateTable.getItems().add(sourceState);
        }

        LinkedList<BufferState> buffer = newState.getBuffer();
        for (BufferState bufferState : buffer) {
            bufferStateTable.getItems().add(bufferState);
        }

        LinkedList<DeviceState> devices = newState.getDevices();
        for (int i = 0; i < numberOfDevices; i++) {
            DeviceState deviceState = devices.get(i);
            deviceStateTable.getItems().add(deviceState);
        }
    }
}
