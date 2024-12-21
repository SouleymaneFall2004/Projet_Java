package org.example.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo.controllers.utils.Factory;
import org.example.demo.controllers.utils.GlobalFilter;
import org.example.demo.core.implementations.ControllersImpl;
import org.example.demo.data.entities.Account;
import org.example.demo.data.entities.enums.Role;
import org.example.demo.data.entities.enums.State;
import org.example.demo.services.interfaces.IServiceAccount;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAccountList extends ControllersImpl implements Initializable {
    private final Factory factory = new Factory();
    private IServiceAccount serviceAccount;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Account, Integer> idColumn;
    @FXML
    private TableColumn<Account, String> loginColumn;
    @FXML
    private TableColumn<Account, String> roleColumn;
    @FXML
    private TableColumn<Account, String> stateColumn;
    @FXML
    private TableColumn<Account, Void> editColumn;
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceAccount = factory.getServiceAccount();
        filterComboBox.setValue(GlobalFilter.getCurrentComboBoxFilter());
        loadAccounts();
        configureEditColumn();
    }

    private void loadAccounts() {
        if (idColumn.getCellValueFactory() == null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
            stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        }

        List<Account> accounts = switch (GlobalFilter.getCurrentComboBoxFilter()) {
            case "admins" -> serviceAccount.fetchAllAdminAccounts();
            case "shopkeepers" -> serviceAccount.fetchAllShopkeeperAccounts();
            case "clients" -> serviceAccount.fetchAllClientAccounts();
            case "enabled" -> serviceAccount.fetchAllEnabledAccounts();
            case "disabled" -> serviceAccount.fetchAllDisabledAccounts();
            default -> serviceAccount.show();
        };

        ObservableList<Account> observableAccounts = FXCollections.observableArrayList(accounts);
        accountTableView.setItems(observableAccounts);
    }

    private void configureEditColumn() {
        editColumn.setCellFactory(_ -> new TableCell<>() {
            private final Button editButton = new Button("Toggle state");

            {
                editButton.setOnAction(_ -> {
                    accountTableView.getSortOrder().clear();
                    Account account = getTableView().getItems().get(getIndex());
                    if (account.getState() == State.ENABLED) {
                        serviceAccount.editState(account, State.DISABLED);
                    } else {
                        serviceAccount.editState(account, State.ENABLED);
                    }
                    redirectTo("/org/example/demo/admin/accountList.fxml");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    @FXML
    private void saveAccount() {
        String login = loginField.getText();
        String password = passwordField.getText();
        Object role = roleComboBox.getValue();

        if (role == null) {
            displayErrorMessage("You need a role in...");
        } else {
            Role actualRole = checkRole(role);
            Account selectedAccount = new Account(login, password, actualRole, State.ENABLED);
            if (selectedAccount.getLogin().compareTo("") == 0 || selectedAccount.getPassword().compareTo("") == 0) {
                displayErrorMessage("Please fill in all values");
            } else {
                serviceAccount.save(selectedAccount);
                redirectTo("/org/example/demo/admin/accountList.fxml");
            }
        }
    }

    @FXML
    private void filter() {
        String filterValue = filterComboBox.getValue();
        GlobalFilter.setCurrentComboBoxFilter(filterValue);
        redirectTo("/org/example/demo/admin/accountList.fxml");
    }

    @Override
    public void displayErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
        loginField.clear();
        passwordField.clear();
    }
}