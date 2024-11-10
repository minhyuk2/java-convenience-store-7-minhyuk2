package store.domain;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

//잘못된 입력에 대한 검출도 필요하고 주석 제거하는 부분도 반드시 진행되어야 한다.
public class Order {
    private final String name;
    private int quantity;

    public Order(String name,int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    //getter를 쓰기 대신에 여기서 최대한 처리할 수 있게끔 만들어야 한다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity && Objects.equals(name, order.name);
    }
    public Product findMatchingProduct(List<Product> products) {
        return products.stream()
                .filter(product -> product.equalName(this.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
    }
    public int getQuantity() {
        return quantity;
    }
}
