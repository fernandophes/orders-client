package br.edu.ufersa.cc.sd.services;

import java.util.List;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderService {

    private final OrderRepository orderRepository;

    private static OrderService instance;

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService(OrderRepository.getInstance());
        }

        return instance;
    }

    public Long countAll() {
        return orderRepository.countAll();
    }

    public List<Order> listAll() {
        return orderRepository.listAll();
    }

    public void create(final Order order) {
        orderRepository.save(order);
    }

    public Order findByCode(final Long code) {
        return orderRepository.findByCode(code);
    }

    public void update(final Order order) {
        orderRepository.update(order);
    }

    public void delete(final Order order) {
        orderRepository.delete(order);
    }

}
