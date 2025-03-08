package br.edu.ufersa.cc.sd.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.dto.Request;
import br.edu.ufersa.cc.sd.dto.Response;
import br.edu.ufersa.cc.sd.exceptions.ConnectionException;
import br.edu.ufersa.cc.sd.exceptions.OperationException;

public class SocketService {

    private static final Logger LOG = LoggerFactory.getLogger(SocketService.class.getSimpleName());

    private static final String HOST = "localhost";
    private static final Integer PORT = 8484;

    public <T extends Serializable> Response<T> call(final Request<T> request) {
        return callAndTransform(request);
    }

    public <I extends Serializable, O extends Serializable> Response<O> callAndTransform(final Request<I> request) {
        LOG.info("Iniciando cliente...");
        try (final var socket = new Socket(HOST, PORT)) {
            LOG.info("Conectado ao servidor");
            final var output = new ObjectOutputStream(socket.getOutputStream());
            final var input = new ObjectInputStream(socket.getInputStream());

            LOG.info("Enviando requisição...");
            output.writeObject(request);
            output.flush();

            LOG.info("Aguardando resposta...");
            @SuppressWarnings("unchecked")
            final var response = (Response<O>) input.readObject();

            input.close();
            output.close();

            LOG.info("Conexão encerrada");
            return response;
        } catch (final IOException e) {
            throw new ConnectionException(e);
        } catch (final ClassNotFoundException e) {
            throw new OperationException("A resposta não pôde ser interpretada", e);
        }
    }

}
