package store.domain.dto;

import store.domain.Order;

import java.util.List;

public class OrderDTO {
    private List<Order> orders;

    public OrderDTO(List<Order> orderList) {
        this.orders = orderList;
    }
    //여기서 최대한 처리할 수 있도록 로직을 구성해야함
    public List<Order> getOrders() {
        return orders;
    }
}
