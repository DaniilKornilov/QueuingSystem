package ru.poly.smo.graphics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFormController {

    @FXML
    private Button stepMode;

    @FXML
    private Button autoMode;

    @FXML
    private TextField sources;

    @FXML
    private TextField lambda;

    @FXML
    private TextField requests;

    @FXML
    private TextField devices;

    @FXML
    private TextField a;

    @FXML
    private TextField b;

    @FXML
    private TextField buffer;

    @FXML
    private Label info;

    @FXML
    private TextField steps;

    @FXML
    private CheckBox isGeneratingNew;

    private void setTextFields() {
        sources.setText("5");
        lambda.setText("1");
        requests.setText("100");
        devices.setText("8");
        a.setText("1");
        b.setText("2");
        buffer.setText("3");
        steps.setText("10");
    }

    private boolean areFieldsValid() {
        return Integer.parseInt(sources.getText()) > 0
                && Double.parseDouble(lambda.getText()) > 0
                && Integer.parseInt(requests.getText()) > 0
                && Integer.parseInt(devices.getText()) > 0
                && Double.parseDouble(a.getText()) > 0
                && Double.parseDouble(b.getText()) > 0
                && Double.parseDouble(a.getText()) < Double.parseDouble(b.getText())
                && Integer.parseInt(buffer.getText()) > 0;
    }

    @FXML
    void initialize() {
        setTextFields();

        autoMode.setOnAction(event -> {
            if (areFieldsValid()) {
                setValues();

                autoMode.setDisable(true);
                Stage main = (Stage) autoMode.getScene().getWindow();
                main.close();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/AutoMode.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } else {
                info.setText("Fields must be more than 0!");
            }
        });

        stepMode.setOnAction(event -> {
            if (areFieldsValid() && Integer.parseInt(steps.getText()) > 0) {
                setValues();
                ValuesManager.setNumberOfSteps(Integer.parseInt(steps.getText()));

                stepMode.setDisable(true);
                Stage main = (Stage) autoMode.getScene().getWindow();
                main.close();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/StepMode.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } else {
                info.setText("Fields must be more than 0!");
            }
        });
    }

    private void setValues() {
        ValuesManager.setNumberOfSources(Integer.parseInt(sources.getText()));
        ValuesManager.setLambda(Double.parseDouble(lambda.getText()));
        ValuesManager.setNumberOfRequests(Integer.parseInt(requests.getText()));
        ValuesManager.setNumberOfDevices(Integer.parseInt(devices.getText()));
        ValuesManager.setA(Double.parseDouble(a.getText()));
        ValuesManager.setB(Double.parseDouble(b.getText()));
        ValuesManager.setNumberOfBufferElements(Integer.parseInt(buffer.getText()));
        ValuesManager.setIsNew(isGeneratingNew.isSelected());
    }
}
