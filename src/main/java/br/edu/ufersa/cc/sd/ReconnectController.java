package br.edu.ufersa.cc.sd;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.exceptions.ConnectionException;
import br.edu.ufersa.cc.sd.exceptions.CustomException;
import br.edu.ufersa.cc.sd.exceptions.OperationException;
import br.edu.ufersa.cc.sd.services.LocalizationService;
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
        addressField.setText(LocalizationService.getHost());
        portField.setText(LocalizationService.getPort().toString());
    }

    @FXML
    private void retry() throws IOException {
        LocalizationService.setHost(addressField.getText());
        LocalizationService.setPort(Integer.parseInt(portField.getText()));

        try {
            LocalizationService.localizeAndUpdate();
            App.setRoot("listAll");
        } catch (final IOException | CustomException e) {
            final var alert = new Alert(AlertType.ERROR);
            Throwable exception = e;

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

            alert.setHeaderText(alert.getTitle());
            alert.show();
            LOG.error("", e);
        }
    }

    @FXML
    private void verifyRetryButton(final Event ignore) {
        retryButton.setDisable(addressField.getText().isEmpty() || portField.getText().isEmpty());
    }

}