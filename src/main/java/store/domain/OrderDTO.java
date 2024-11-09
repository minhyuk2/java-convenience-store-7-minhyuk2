package store.domain;

import java.util.List;

public class OrderDTO {
    private List<Order> orderList;

    public OrderDTO(List<Order> orderList) {
        this.orderList = orderList;
    }
    //여기서 최대한 처리할 수 있도록 로직을 구성해야함
}
