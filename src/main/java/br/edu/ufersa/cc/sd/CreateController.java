package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.exceptions.ConnectionException;
import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateController.class.getSimpleName());
    private OrderService service = new OrderService();

    @FXML
    private TextField nameShow;

    @FXML
    private TextField descriptionShow;

    @FXML
    private Button saveButton;

    @FXML
    private void switchToListAll() throws IOException {
        try {
            App.setRoot("listAll");
        } catch (final IOException e) {
            final var alert = new Alert(AlertType.ERROR);
            var exception = e.getCause();

            var mustReconnect = false;
            while (exception != null) {
                if (exception instanceof ConnectionException) {
                    alert.setTitle("Conexão perdida");
                    alert.setContentText("Retornando ao servidor de localização");
                    mustReconnect = true;
                    break;
                } else {
                    alert.setTitle("Ocorreu um erro");
                    alert.setContentText(e.getMessage());
                }

                exception = exception.getCause();
            }

            alert.setHeaderText(alert.getTitle());
            alert.show();
            LOG.error("", e);

            if (mustReconnect) {
                App.setRoot("reconnect");
            }
        }
    }

    @FXML
    private void save() throws IOException {
        final var order = new Order()
                .setName(nameShow.getText())
                .setDescription(descriptionShow.getText())
                .setCreatedAt(LocalDateTime.now());
        service.create(order);
        switchToListAll();
    }

    @FXML
    private void verifySaveButton(Event ignore) {
        saveButton.setDisable(nameShow.getText().isEmpty());
    }

}