package com.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.application.model.Patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import com.application.model.PatientDAO;
import com.application.utils.NotificationUtil;

public class ShowPatient extends EditPatient {
    @FXML
    private TableView<Patient> tableContainer;
    @FXML
    private TableColumn<Patient, String> indexCol, nameCol, addressCol, patientIDCol, birthCol;
    private PatientDAO patientDAO;
    private EditPatient editPatientController;

    public ShowPatient() {
        this.patientDAO = new PatientDAO();
        this.editPatientController = new EditPatient();
    }

    // private void setData(TableView<Patient> tableContainer) {
    // Patient selected = selectedPatient(tableContainer);

    // if (selected != null) {
    // } else {
    // NotificationUtil.showNotification("No row selected", "ERROR");
    // }
    // }

    private static Patient selectedPatient(TableView<Patient> tableContainer) {
        Patient selected = tableContainer.getSelectionModel().getSelectedItem();
        return selected;
    }

    @FXML
    private void deleteData(ActionEvent evt) throws IOException {
        Patient selectedPatient = selectedPatient(tableContainer);
        if (selectedPatient != null) {
            try {
                patientDAO.deletePatient(selectedPatient);
                tableContainer.getItems().remove(selectedPatient);
            } catch (SQLException e) {
                e.printStackTrace();
                NotificationUtil.showNotification("Failed to delete data", "ERROR");
            }
        } else {
            NotificationUtil.showNotification("No row selected", "ERROR");

        }
    }

    @FXML
    private void showData() {
        try {
            ObservableList<Patient> patients = FXCollections.observableArrayList(patientDAO.getAllPatients());

            tableContainer.setItems(patients);
            indexCol.setCellValueFactory(cell -> new SimpleStringProperty(patients.indexOf(cell.getValue()) + 1 + ""));
            nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
            addressCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
            birthCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBirth() + ""));
            patientIDCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPatientID()));

        } catch (SQLException e) {
            e.printStackTrace();
            NotificationUtil.showNotification("Failed to show data", "ERROR");
        }
    }

    @FXML
    private void toEditForm(ActionEvent evt) throws IOException {
        try {
            Patient selectedPatient = selectedPatient(tableContainer);
            if (selectedPatient != null) {
                editPatientController.loadPatientData(selectedPatient.getId());

            } else {
                NotificationUtil.showNotification("No row selected", "ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
            NotificationUtil.showNotification("Failed to load data", "ERROR");
        }
    }

    @FXML
    private void toMenu(ActionEvent evt) throws IOException {
        Navigation.navigateTo("Home");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
        tableContainer.setPlaceholder(new Label("No records found"));
    }

}
