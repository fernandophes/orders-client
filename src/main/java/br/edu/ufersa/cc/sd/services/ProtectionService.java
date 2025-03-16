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

            var mustShowError = true;
            var mustReconnect = false;
            while (exception != null) {
                var mustBreak = false;
                if (exception instanceof OperationException) {
                    alert.setTitle("Operação inválida");
                    log.error(alert.getTitle());
                    mustBreak = true;
                } else if (exception instanceof ConnectionException) {
                    // Localizar novo endereço do Proxy
                    LocalizationService.localizeAndUpdate();

                    // Tentar executar a ação uma 2ª vez
                    try {
                        action.run();
                        mustShowError = false;
                        mustReconnect = false;
                    } catch (final IOException | CustomException ignore) {
                        alert.setTitle("Conexão perdida");
                        alert.setContentText("Retornando ao servidor de localização");
                        log.error(alert.getTitle());
                        mustReconnect = true;
                    }

                    mustBreak = true;
                } else {
                    alert.setTitle("Ocorreu um erro");
                    alert.setContentText(e.getMessage());
                    log.error(alert.getTitle(), e);
                }

                if (mustBreak) {
                    break;
                }

                exception = exception.getCause();
            }

            if (mustShowError) {
                alert.setHeaderText(alert.getTitle());
                alert.show();
            }

            if (mustReconnect) {
                LocalizationService.setHost(Constants.DEFAULT_HOST);
                LocalizationService.setPort(Constants.LOCALIZATION_PORT);
                App.setRoot("reconnect");
            }
        }
    }

}
