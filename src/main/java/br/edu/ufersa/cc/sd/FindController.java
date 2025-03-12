package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.edu.ufersa.cc.sd.exceptions.OperationException;
import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import br.edu.ufersa.cc.sd.services.ProtectionService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FindController {

    private static final Logger LOG = LogManager.getLogger(FindController.class.getSimpleName());
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");

    private OrderService service = new OrderService();

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
        ProtectionService.protect(() -> App.setRoot("listAll"), LOG);
    }

    @FXML
    private void readOrder() throws IOException {
        ProtectionService.protect(() -> {
            try {
                final var code = Long.parseLong(codeShow.getText());
                final var order = service.findByCode(code);

                if (order == null) {
                    throw new OperationException("A ordem não existe");
                }

                showOrder(order);
            } catch (final NumberFormatException e) {
                final var alert = new Alert(AlertType.ERROR);
                alert.setTitle("Código inválido");
                alert.setContentText("O código precisa ser um número válido");
                alert.show();
            } catch (final OperationException e) {
                final var alert = new Alert(AlertType.WARNING);
                alert.setTitle("Ordem não encontrada");
                alert.setContentText("O código informado não pertence a nenhuma ordem");
                alert.show();
            }
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
        }, LOG);
    }

    @FXML
    private void delete() throws IOException {
        ProtectionService.protect(() -> service.delete(currentOrder), LOG);
    }

    @FXML
    private void markAsDone() throws IOException {
        ProtectionService.protect(() -> {
            currentOrder.setDoneAt(LocalDateTime.now());
            service.update(currentOrder);
            showOrder(currentOrder);
        }, LOG);
    }

    @FXML
    private void verifySaveButton(Event ignore) {
        updateButton.setDisable(nameShow.getText().isEmpty());
    }

}