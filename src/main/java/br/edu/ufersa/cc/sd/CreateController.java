package br.edu.ufersa.cc.sd;

import java.io.IOException;
import java.time.LocalDateTime;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.OrderService;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateController {

    private OrderService service = OrderService.getInstance();

    @FXML
    private TextField nameShow;

    @FXML
    private TextField descriptionShow;

    @FXML
    private Button saveButton;

    @FXML
    private void switchToListAll() throws IOException {
        App.setRoot("listAll");
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