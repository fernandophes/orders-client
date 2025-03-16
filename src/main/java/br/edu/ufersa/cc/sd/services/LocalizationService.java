package br.edu.ufersa.cc.sd.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.edu.ufersa.cc.sd.dto.Request;
import br.edu.ufersa.cc.sd.dto.Response;
import br.edu.ufersa.cc.sd.enums.Operation;
import br.edu.ufersa.cc.sd.enums.ResponseStatus;
import br.edu.ufersa.cc.sd.exceptions.ConnectionException;
import br.edu.ufersa.cc.sd.exceptions.OperationException;
import br.edu.ufersa.cc.sd.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class LocalizationService {

    private static final Logger LOG = LogManager.getLogger(LocalizationService.class.getSimpleName());

    @Getter
    @Setter
    private static String host = Constants.DEFAULT_HOST;

    @Getter
    @Setter
    private static Integer port = Constants.LOCALIZATION_PORT;

    public static Response<InetSocketAddress> localizeAndUpdate() {
        LOG.info("Obtendo endereço do Proxy através do Servidor de Localização...");
        final var service = new LocalizationService();
        final var response = service.call(new Request<>(Operation.LOCALIZE, InetSocketAddress.class));

        if (response.getStatus() == ResponseStatus.OK) {
            final var address = response.getItem();

            LOG.info("Localização do Servidor Proxy recebida: {}", address);
            SocketService.setHost(address.getHostString());
            SocketService.setPort(address.getPort());

            LOG.info("Utilizando novo endereço", address);
        } else {
            throw new OperationException(response.getMessage());
        }

        return response;
    }

    public <T extends Serializable> Response<T> call(final Request<T> request) {
        return callAndTransform(request);
    }

    public <I extends Serializable, O extends Serializable> Response<O> callAndTransform(final Request<I> request) {
        LOG.info("Iniciando cliente...");
        try (final var socket = new Socket(host, port)) {
            LOG.info("Conectado ao servidor");
            final var output = new ObjectOutputStream(socket.getOutputStream());
            final var input = new ObjectInputStream(socket.getInputStream());

            LOG.info("Enviando requisição...");
            output.writeObject(request);
            output.flush();

            // Preparar timeout
            final var timer = new Timer();
            final TimerTask timeout = new TimerTask() {
                public void run() {
                    try {
                        input.close();
                        output.close();
                        throw new ConnectionException("Timeout: o serviço não respondeu a tempo");
                    } catch (final IOException ignore) {
                        // Ignorar
                    }
                }
            };
            timer.schedule(timeout, 30_000);

            LOG.info("Aguardando resposta...");
            @SuppressWarnings("unchecked")
            final var response = (Response<O>) input.readObject();
            timeout.cancel();

            input.close();
            output.close();

            LOG.info("Conexão encerrada");
            return response;
        } catch (final ConnectException e) {
            throw new ConnectionException("A conexão com " + host + ":" + port + " foi recusada", e);
        } catch (final IOException e) {
            throw new ConnectionException(e);
        } catch (final ClassNotFoundException e) {
            throw new OperationException("A resposta não pôde ser interpretada", e);
        }
    }

}
