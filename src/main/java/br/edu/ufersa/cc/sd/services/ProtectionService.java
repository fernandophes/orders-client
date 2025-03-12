package br.edu.ufersa.cc.sd.services;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import br.edu.ufersa.cc.sd.App;
import br.edu.ufersa.cc.sd.exceptions.ConnectionException;
import br.edu.ufersa.cc.sd.exceptions.CustomException;
import br.edu.ufersa.cc.sd.exceptions.OperationException;
import br.edu.ufersa.cc.sd.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public interface ProtectionService {

    @FunctionalInterface
    public interface Action {
        void run() throws IOException, CustomException;
    }

    public static void protect(final Action action, final Logger log) throws IOException {
        try {
            action.run();
        } catch (final IOException | CustomException e) {
            final var alert = new Alert(AlertType.ERROR);
            Throwable exception = e;

            var mustReconnect = false;
            while (exception != null) {
                if (exception instanceof OperationException) {
                    alert.setTitle("Operação inválida");
                    log.error(alert.getTitle());
                    break;
                } else if (exception instanceof ConnectionException) {
                    alert.setTitle("Conexão perdida");
                    alert.setContentText("Retornando ao servidor de localização");
                    log.error(alert.getTitle());
                    mustReconnect = true;
                    break;
                } else {
                    alert.setTitle("Ocorreu um erro");
                    alert.setContentText(e.getMessage());
                    log.error(alert.getTitle(), e);
                }

                exception = exception.getCause();
            }

            alert.setHeaderText(alert.getTitle());
            alert.show();

            if (mustReconnect) {
                LocalizationService.setHost(Constants.DEFAULT_HOST);
                LocalizationService.setPort(Constants.LOCALIZATION_PORT);
                App.setRoot("reconnect");
            }
        }
    }

}
