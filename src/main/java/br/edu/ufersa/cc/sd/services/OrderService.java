package br.edu.ufersa.cc.sd.services;

import java.util.List;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.repositories.OrderRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> list() {
        return orderRepository.listAll();
    }

    public void create(final Order order) {
        orderRepository.save(order);
    }

    public Order read(final Long code) {
        return orderRepository.findByCode(code);
    }

    public void update(final Order order) {
        orderRepository.update(order);
    }

    public void delete(final Order order) {
        orderRepository.delete(order);
    }

}
