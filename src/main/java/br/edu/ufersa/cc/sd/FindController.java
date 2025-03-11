package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FindController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");

    private OrderService service = OrderService.getInstance();

    private Order currentOrder;

    @FXML
    private Label labelShow;

    @FXML
    private Label createdAtShow;

    @FXML
    private Label doneAtShow;

    @FXML
    private TextField codeShow;

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
    private void switchToListAll() throws IOException {
        App.setRoot("listAll");
    }

    @FXML
    private void readOrder() {
        final var code = Long.parseLong(codeShow.getText());
        final var order = service.findByCode(code);
        showOrder(order);
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
    private void update() {
        currentOrder.setName(nameShow.getText());
        currentOrder.setDescription(descriptionShow.getText());
        service.update(currentOrder);
    }

    @FXML
    private void delete() {
        service.delete(currentOrder);
    }

    @FXML
    private void markAsDone() {
        currentOrder.setDoneAt(LocalDateTime.now());
        service.update(currentOrder);
        showOrder(currentOrder);
    }

    @FXML
    private void verifySaveButton(Event ignore) {
        updateButton.setDisable(nameShow.getText().isEmpty());
    }

}