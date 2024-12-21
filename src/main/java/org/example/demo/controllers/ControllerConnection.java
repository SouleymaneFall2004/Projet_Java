package org.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.controllers.utils.Factory;
import org.example.demo.controllers.utils.Session;
import org.example.demo.core.implementations.ControllersImpl;
import org.example.demo.data.entities.Account;
import org.example.demo.data.entities.enums.State;
import org.example.demo.services.interfaces.IServiceAccount;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
@Setter

public class ControllerConnection extends ControllersImpl implements Initializable {
    private final Factory factory = new Factory();
    private IServiceAccount serviceAccount;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceAccount = factory.getServiceAccount();
    }

    @FXML
    private void handleLoginAction() {
        String login = loginField.getText();
        String password = passwordField.getText();

        Account currentAccount = serviceAccount.fetchByLoginAndPassword(login, password);

        if (currentAccount == null) {
            displayErrorMessage("Login or password incorrect.");
        } else if (currentAccount.getState() == State.DISABLED) {
            displayErrorMessage("This account is disabled.");
        } else {
            Session.setAccount(currentAccount);
            switch (currentAccount.getRole()) {
                case ADMIN -> redirectTo("/org/example/demo/admin/accountList.fxml");
                case SHOPKEEPER -> redirectTo("/org/example/demo/shopkeeper/clientList.fxml");
                case CLIENT -> redirectTo("/org/example/demo/client/debtListClient.fxml");
            }
        }
    }

    @Override
    public void displayErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
        loginField.clear();
        passwordField.clear();
    }
}