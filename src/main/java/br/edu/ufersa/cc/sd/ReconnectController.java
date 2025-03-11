package br.edu.ufersa.cc.sd;

import java.io.IOException;

import br.edu.ufersa.cc.sd.services.SocketService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReconnectController {

    @FXML
    private TextField addressField;

    @FXML
    private TextField portField;

    @FXML
    private Button retryButton;

    @FXML
    protected void initialize() {
        addressField.setText(SocketService.getHost());
        portField.setText(SocketService.getPort().toString());
    }

    @FXML
    private void retry() throws IOException {
        SocketService.setHost(addressField.getText());
        SocketService.setPort(Integer.parseInt(portField.getText()));
        App.setRoot("listAll");
    }
    @FXML
    private void verifyRetryButton(Event ignore) {
        retryButton.setDisable(addressField.getText().isEmpty() || portField.getText().isEmpty());
    }

}