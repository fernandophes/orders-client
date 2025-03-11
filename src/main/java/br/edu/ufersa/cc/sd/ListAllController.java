package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import br.edu.ufersa.cc.sd.services.ProtectionService;
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

    private static final Logger LOG = LoggerFactory.getLogger(ListAllController.class.getSimpleName());
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");

    private OrderService service = new OrderService();

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

    @FXML
    private Label countLabel;

    private Order currentOrder;

    @FXML
    private Label labelShow;

    @FXML
    private Label createdAtShow;

    @FXML
    private Label doneAtShow;

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
    protected void initialize() throws IOException {
        refreshTable();
    }

    @FXML
    private void switchToCreate() throws IOException {
        App.setRoot("create");
    }

    @FXML
    private void switchToFind() throws IOException {
        App.setRoot("find");
    }

    @FXML
    private void refreshTable() throws IOException {
        ProtectionService.protect(() -> {
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

            final var orders = FXCollections.observableArrayList(service.listAll());
            table.setItems(orders);

            final var count = service.countAll();
            countLabel.setText("Total: " + count);
        }, LOG);
    }

    @FXML
    private void showOrder(final Order order) {
        currentOrder = order;

        labelShow.setText("Detalhes da Ordem #" + order.getCode());
        nameShow.setText(order.getName());
        descriptionShow.setText(order.getDescription());
        createdAtShow.setText("Criada em " + FORMATTER.format(order.getCreatedAt()));

        if (order.getDoneAt() != null) {
            doneAtShow.setText("Concluída em " + FORMATTER.format(order.getDoneAt()));
        } else {
            doneAtShow.setText("Ainda não concluída");
        }

        nameShow.setDisable(false);
        descriptionShow.setDisable(false);
        createdAtShow.setVisible(true);
        doneAtShow.setVisible(true);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
        markAsDoneButton.setDisable(order.getDoneAt() != null);
    }

    @FXML
    private void update() throws IOException {
        ProtectionService.protect(() -> {
            currentOrder.setName(nameShow.getText());
            currentOrder.setDescription(descriptionShow.getText());
            service.update(currentOrder);
            refreshTable();
        }, LOG);
    }

    @FXML
    private void delete() throws IOException {
        ProtectionService.protect(() -> {
            service.delete(currentOrder);
            refreshTable();
        }, LOG);
    }

    @FXML
    private void markAsDone() throws IOException {
        ProtectionService.protect(() -> {
            currentOrder.setDoneAt(LocalDateTime.now());
            service.update(currentOrder);
            showOrder(currentOrder);
            refreshTable();
        }, LOG);
    }

}