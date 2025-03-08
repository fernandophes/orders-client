package br.edu.ufersa.cc.sd.repositories;

import java.util.List;

import br.edu.ufersa.cc.sd.dto.Request;
import br.edu.ufersa.cc.sd.dto.Response;
import br.edu.ufersa.cc.sd.enums.Operation;
import br.edu.ufersa.cc.sd.enums.ResponseStatus;
import br.edu.ufersa.cc.sd.exceptions.OperationException;
import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.services.SocketService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderRepository {

    private final SocketService socketService;

    public List<Order> listAll() {
        final var request = new Request<>(Operation.LIST, Order.class);
        final var response = socketService.call(request);

        if (ResponseStatus.ERROR.equals(response.getStatus())) {
            throw new OperationException(response.getMessage());
        }

        return response.getItems();
    }

    public void save(final Order order) {
        final var request = new Request<>(Operation.CREATE, order);
        final var response = socketService.call(request);

        if (ResponseStatus.ERROR.equals(response.getStatus())) {
            throw new OperationException(response.getMessage());
        }
    }

    public Order findByCode(final Long code) {
        final var request = new Request<>(Operation.READ, new Order().setCode(code));
        final var response = socketService.call(request);

        if (ResponseStatus.ERROR.equals(response.getStatus())) {
            throw new OperationException(response.getMessage());
        }

        return response.getItems().get(0);
    }

    public void update(final Order order) {
        final var request = new Request<>(Operation.UPDATE, order);
        final var response = socketService.call(request);

        if (ResponseStatus.ERROR.equals(response.getStatus())) {
            throw new OperationException(response.getMessage());
        }
    }

    public void delete(final Order order) {
        final var request = new Request<>(Operation.DELETE, order);
        final var response = socketService.call(request);

        if (ResponseStatus.ERROR.equals(response.getStatus())) {
            throw new OperationException(response.getMessage());
        }
    }

    public Long countAll() {
        final var request = new Request<>(Operation.COUNT, Order.class);
        final Response<Long> response = socketService.callAndTransform(request);

        if (ResponseStatus.ERROR.equals(response.getStatus())) {
            throw new OperationException(response.getMessage());
        }

        return response.getItems().get(0);
    }

}
