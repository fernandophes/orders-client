package br.edu.ufersa.cc.sd.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.dto.Request;
import br.edu.ufersa.cc.sd.dto.Response;
import br.edu.ufersa.cc.sd.enums.ResponseStatus;
import br.edu.ufersa.cc.sd.models.Order;

public class ServerSimulator implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ServerSimulator.class.getSimpleName());

    private static final LinkedList<Order> ORDERS = new LinkedList<>(List.of(
            new Order().setCode(1L).setName("1a ordem").setDescription("Primeira ordem")
                    .setCreatedAt(LocalDateTime.now()),
            new Order().setCode(2L).setName("2a ordem").setDescription("Segunda ordem")
                    .setCreatedAt(LocalDateTime.now()),
            new Order().setCode(3L).setName("3a ordem").setDescription("Terceira ordem")
                    .setCreatedAt(LocalDateTime.now())));

    private ServerSocket serverSocket;
    private boolean isAlive = true;

    @Override
    public void run() {
        LOG.info("Servidor iniciado");

        try {
            serverSocket = new ServerSocket(8484);
            new Thread(() -> waitForClients(serverSocket)).start();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        isAlive = false;
        serverSocket = null;
    }

    private void waitForClients(final ServerSocket serverSocket) {
        try {
            while (isAlive) {
                LOG.info("Aguardando clientes...");
                final var client = serverSocket.accept();
                new Thread(() -> handleClient(client)).start();
            }
        } catch (final SocketException e) {
            LOG.info("Servidor encerrado.");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(final Socket client) {
        LOG.info("Cliente conectado: {}", client.getInetAddress());
        try {
            final var output = new ObjectOutputStream(client.getOutputStream());
            final var input = new ObjectInputStream(client.getInputStream());

            LOG.info("Aguardando mensagens...");

            @SuppressWarnings("unchecked")
            final var request = (Request<Order>) input.readObject();
            final var item = request.getItem();
            LOG.info("Simulando operação {}...", request.getOperation());

            final Response<Serializable> response;
            switch (request.getOperation()) {
                case LIST:
                    response = new Response<>(new ArrayList<>(ORDERS));
                    break;

                case CREATE:
                    final var maxCode = ORDERS.stream().map(order -> order.getCode()).max(Long::compareTo).get();
                    item.setCode(maxCode + 1);
                    ORDERS.add(item);
                    response = new Response<>(ResponseStatus.OK);
                    break;

                case UPDATE:
                    ORDERS.stream().filter(order -> order.getCode().equals(item.getCode()))
                            .findFirst().orElseThrow()
                            .setName(item.getName())
                            .setDescription(item.getDescription())
                            .setDoneAt(item.getDoneAt());
                    response = new Response<>(ResponseStatus.OK);
                    break;

                case DELETE:
                    ORDERS.remove(item);
                    response = new Response<>(ResponseStatus.OK);
                    break;

                case COUNT:
                default:
                    response = new Response<>(ORDERS.size());
                    break;
            }

            output.writeObject(response);

            client.close();
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
