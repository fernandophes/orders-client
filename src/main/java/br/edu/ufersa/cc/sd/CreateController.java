package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import br.edu.ufersa.cc.sd.services.ProtectionService;
import javafx.event.Event;
import javafx.fxml.FXML;
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
        ProtectionService.protect(() -> App.setRoot("listAll"), LOG);
    }

    @FXML
    private void save() throws IOException {
        ProtectionService.protect(() -> {
            final var order = new Order()
                    .setName(nameShow.getText())
                    .setDescription(descriptionShow.getText())
                    .setCreatedAt(LocalDateTime.now());
            service.create(order);
            switchToListAll();
        }, LOG);
    }

    @FXML
    private void verifySaveButton(Event ignore) {
        saveButton.setDisable(nameShow.getText().isEmpty());
    }

}