package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

public class ListAllController {

    private OrderService service = OrderService.getInstance();

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Long> codeColumn;

    @FXML
    private TableColumn<Order, String> nameColumn;

    @FXML
    private TableColumn<Order, String> descriptionColumn;

    @FXML
    private TableColumn<Order, String> createdAtColumn;

    @FXML
    private TableColumn<Order, String> doneAtColumn;

    private Order currentOrder;

    @FXML
    private Label labelShow;

    @FXML
    private TextField nameShow;

    @FXML
    private TextField descriptionShow;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button markAsDoneButton;

    @FXML
    protected void initialize() {
        refreshTable();
    }

    @FXML
    private void switchToHome() throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void switchToCreate() throws IOException {
        App.setRoot("create");
    }

    @FXML
    private void refreshTable() {
        table.setRowFactory(tab -> {
            final var row = new TableRow<Order>();

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    showOrder(row.getItem());
                }
            });

            return row;
        });

        codeColumn.setCellValueFactory(new PropertyValueFactory<Order, Long>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("description"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("createdAt"));
        doneAtColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("doneAt"));

        final var orders = FXCollections.observableArrayList(service.list());
        table.setItems(orders);
    }

    @FXML
    private void showOrder(final Order order) {
        currentOrder = order;

        labelShow.setText("Detalhes da Ordem #" + order.getCode());
        nameShow.setText(order.getName());
        descriptionShow.setText(order.getDescription());

        nameShow.setDisable(false);
        descriptionShow.setDisable(false);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
        markAsDoneButton.setDisable(order.getDoneAt() != null);
    }

    @FXML
    private void update() {
        currentOrder.setName(nameShow.getText());
        currentOrder.setDescription(descriptionShow.getText());
        service.update(currentOrder);
        refreshTable();
    }

    @FXML
    private void delete() {
        service.delete(currentOrder);
        refreshTable();
    }

    @FXML
    private void markAsDone() {
        currentOrder.setDoneAt(LocalDateTime.now());
        service.update(currentOrder);
        refreshTable();
    }

}