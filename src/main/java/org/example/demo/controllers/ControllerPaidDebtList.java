package org.example.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo.controllers.utils.Factory;
import org.example.demo.core.implementations.ControllersImpl;
import org.example.demo.data.entities.Debt;
import org.example.demo.services.interfaces.IServiceDebt;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPaidDebtList extends ControllersImpl implements Initializable {
    private final Factory factory = new Factory();
    @FXML
    private TableView<Debt> paidDebtTableView;
    @FXML
    private TableColumn<Debt, Integer> idColumn;
    @FXML
    private TableColumn<Debt, String> dateColumn;
    @FXML
    private TableColumn<Debt, Integer> amountTotalColumn;
    @FXML
    private TableColumn<Debt, Integer> amountSentColumn;
    @FXML
    private TableColumn<Debt, Integer> amountMissingColumn;
    private IServiceDebt serviceDebt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceDebt = factory.getServiceDebt();
        loadPaidDebts();
    }

    private void loadPaidDebts() {
        idColumn.prefWidthProperty().bind(paidDebtTableView.widthProperty().multiply(0.20));
        dateColumn.prefWidthProperty().bind(paidDebtTableView.widthProperty().multiply(0.20));
        amountTotalColumn.prefWidthProperty().bind(paidDebtTableView.widthProperty().multiply(0.20));
        amountSentColumn.prefWidthProperty().bind(paidDebtTableView.widthProperty().multiply(0.20));
        amountMissingColumn.prefWidthProperty().bind(paidDebtTableView.widthProperty().multiply(0.20));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountTotalColumn.setCellValueFactory(new PropertyValueFactory<>("amountTotal"));
        amountSentColumn.setCellValueFactory(new PropertyValueFactory<>("amountSent"));
        amountMissingColumn.setCellValueFactory(new PropertyValueFactory<>("amountMissing"));

        List<Debt> paidDebts = serviceDebt.fetchAllPaidDebts();

        ObservableList<Debt> observablePaidDebts = FXCollections.observableArrayList(paidDebts);

        paidDebtTableView.setItems(observablePaidDebts);
    }

    @Override
    public void displayErrorMessage(String message) {

    }
}