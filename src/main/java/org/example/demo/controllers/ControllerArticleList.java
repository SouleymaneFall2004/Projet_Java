package org.example.demo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.demo.controllers.utils.Factory;
import org.example.demo.core.implementations.ControllersImpl;
import org.example.demo.data.entities.Article;
import org.example.demo.services.interfaces.IServiceArticle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerArticleList extends ControllersImpl implements Initializable {
    private final Factory factory = new Factory();
    @FXML
    private TextField labelField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField priceField;
    @FXML
    private Label messageLabel;
    private IServiceArticle serviceArticle;
    @FXML
    private TableView<Article> articleTableView;
    @FXML
    private TableColumn<Article, Integer> idColumn;
    @FXML
    private TableColumn<Article, String> labelColumn;
    @FXML
    private TableColumn<Article, Integer> stockColumn;
    @FXML
    private TableColumn<Article, Integer> priceColumn;
    @FXML
    private TableColumn<Article, Void> editColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceArticle = factory.getServiceArticle();
        loadArticles();
        configureEditColumnWithField();
    }

    private void loadArticles() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        List<Article> articles = serviceArticle.fetchAllAvailableArticles();

        ObservableList<Article> observableArticles = FXCollections.observableArrayList(articles);

        articleTableView.setItems(observableArticles);
    }

    private void configureEditColumnWithField() {
        editColumn.setCellFactory(_ -> new TableCell<>() {
            private final TextField inputField = new TextField();
            private final Button actionButton = new Button("Update");

            {
                actionButton.setOnAction(_ -> {
                    Article article = getTableView().getItems().get(getIndex());
                    int newStock = Integer.parseInt(inputField.getText());
                    if (newStock >= 0) {
                        serviceArticle.editArticleStock(article, newStock);
                        redirectTo("/org/example/demo/admin/articleList.fxml");
                    }
                });

                inputField.setPromptText("Stock");
                inputField.setMaxWidth(50);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(5, inputField, actionButton);
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void saveArticle() {
        try {
            String label = labelField.getText();
            int stock = Integer.parseInt(stockField.getText());
            int price = Integer.parseInt(priceField.getText());

            if (label.compareTo("") == 0) {
                displayErrorMessage("Where's the label???...");
            } else if (stock <= 0) {
                displayErrorMessage("Invalid stock...");
            } else if (price <= 0) {
                displayErrorMessage("Invalid stock...");
            } else {
                Article selectedArticle = new Article(label, stock, price);
                serviceArticle.save(selectedArticle);
                redirectTo("/org/example/demo/admin/articleList.fxml");
            }
        } catch (NumberFormatException e) {
            displayErrorMessage("Stock/Price must be integers...");
        }
    }


    @Override
    public void displayErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
        labelField.clear();
        stockField.clear();
        priceField.clear();
    }
}