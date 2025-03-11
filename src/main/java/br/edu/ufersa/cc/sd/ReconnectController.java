package br.edu.ufersa.cc.sd;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.exceptions.ConnectionException;
import br.edu.ufersa.cc.sd.exceptions.OperationException;
import br.edu.ufersa.cc.sd.services.SocketService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReconnectController {

    private static final Logger LOG = LoggerFactory.getLogger(ReconnectController.class.getSimpleName());

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

        try {
            App.setRoot("listAll");
        } catch (final IOException e) {
            final var alert = new Alert(AlertType.ERROR);
            var exception = e.getCause();

            while (exception != null) {
                alert.setContentText(exception.getMessage());

                if (exception instanceof OperationException) {
                    alert.setTitle("Operação inválida");
                    break;
                } else if (exception instanceof ConnectionException) {
                    alert.setTitle("Conexão recusada");
                    break;
                } else {
                    alert.setTitle("Ocorreu um erro");
                }

                exception = exception.getCause();
            }

            alert.show();
            LOG.error("", e);
        }
    }

    @FXML
    private void verifyRetryButton(final Event ignore) {
        retryButton.setDisable(addressField.getText().isEmpty() || portField.getText().isEmpty());
    }

}