package br.edu.ufersa.cc.sd.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufersa.cc.sd.models.Order;
import br.edu.ufersa.cc.sd.repositories.OrderRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class.getSimpleName());

    private final OrderRepository orderRepository = new OrderRepository();

    public Long countAll() {
        LOG.info("Obtendo a quantidade de ordens...");
        return orderRepository.countAll();
    }

    public List<Order> listAll() {
        LOG.info("Listando todas as ordens...");
        return orderRepository.listAll();
    }

    public void create(final Order order) {
        LOG.info("Cadastrando ordem #{}...", order.getCode());
        orderRepository.save(order);
    }

    public Order findByCode(final Long code) {
        LOG.info("Buscando ordem #{}...", code);
        return orderRepository.findByCode(code);
    }

    public void update(final Order order) {
        LOG.info("Atualizando ordem #{}...", order.getCode());
        orderRepository.update(order);
    }

    public void delete(final Order order) {
        LOG.info("Excluíndo ordem #{}...", order.getCode());
        orderRepository.delete(order);
    }

}
