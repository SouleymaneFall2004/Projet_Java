package org.example.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.demo.controllers.utils.GlobalFilter;
import org.example.demo.controllers.utils.Session;
import org.example.demo.core.implementations.ControllersImpl;
import org.example.demo.data.entities.Debt;
import org.example.demo.data.entities.enums.Status;
import org.example.demo.data.repositories.database.RepositoryDbClient;
import org.example.demo.data.repositories.database.RepositoryDbDebt;
import org.example.demo.data.repositories.interfaces.IRepositoryClient;
import org.example.demo.data.repositories.interfaces.IRepositoryDebt;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerDebtListClient extends ControllersImpl implements Initializable {
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TableColumn<Debt, Integer> idColumn;
    @FXML
    private TableColumn<Debt, Integer> amountTotalColumn;
    @FXML
    private TableColumn<Debt, Integer> amountSentColumn;
    @FXML
    private TableColumn<Debt, Integer> amountMissingColumn;
    @FXML
    private TableColumn<Debt, Status> statusColumn;
    @FXML
    private TableColumn<Debt, Void> editColumn;
    @FXML
    private IRepositoryDebt repositoryDebt;
    @FXML
    private IRepositoryClient repositoryClient;
    @FXML
    private TableView<Debt> debtTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.repositoryDebt = new RepositoryDbDebt();
        this.repositoryClient = new RepositoryDbClient();
        filterComboBox.setValue(GlobalFilter.getCurrentComboBoxFilter());
        loadDebts();
        configureEditColumn();
    }

    private void loadDebts() {
        debtTable(idColumn, amountTotalColumn, amountSentColumn, amountMissingColumn, statusColumn);

        List<Debt> debts = new ArrayList<>();

        if (Session.getAccount().getClient() != null) {
            if (GlobalFilter.getCurrentComboBoxFilter().equals("debt demands")) {
                debts = repositoryDebt.findAllPendingDebtsOfClient(repositoryClient.findById(Session.getAccount().getClient().getId()));
            } else {
                debts = repositoryDebt.findAllUnpaidDebtsOfClient(repositoryClient.findById(Session.getAccount().getClient().getId()));
            }
        }

        assert debts != null;
        ObservableList<Debt> observableDebts = FXCollections.observableArrayList(debts);
        debtTableView.setItems(observableDebts);
    }

    private void configureEditColumn() {
        editColumn.setCellFactory(_ -> new TableCell<>() {
            private final Button acceptButton = new Button("Bump");

            {
                acceptButton.setOnAction(_ -> {
                    Debt debt = getTableView().getItems().get(getIndex());
                    if (debt.getStatus() == Status.CANCELLED) {
                        repositoryDebt.updateStatus(debt, Status.PENDING);
                        redirectTo("/org/example/demo/client/debtListClient.fxml");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableView().getItems().get(getIndex()).getStatus() != Status.CANCELLED) {
                    setGraphic(null);
                } else {
                    setGraphic(acceptButton);
                }
            }
        });
    }


    @Override
    public void displayErrorMessage(String message) {

    }

    @FXML
    private void filter() {
        String filterValue = filterComboBox.getValue();
        GlobalFilter.setCurrentComboBoxFilter(filterValue);
        redirectTo("/org/example/demo/client/debtListClient.fxml");
    }
}