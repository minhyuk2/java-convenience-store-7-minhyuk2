package store.domain;


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

    //getter를 쓰기 대신에 여기서 최대한 처리할 수 있게끔 만들어야 한다.
}
