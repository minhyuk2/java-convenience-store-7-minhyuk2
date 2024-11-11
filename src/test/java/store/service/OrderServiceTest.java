package store.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Order;
import store.domain.Product;
import store.domain.dto.InputDTO;
import store.domain.dto.OrderDTO;
import store.domain.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        List<Product> products = List.of(
                new Product("콜라", 1000, 1500, "null"),
                new Product("사이다", 1000, 1500, "null"));
        productDTO = new ProductDTO(products);
    }

    @Test
    void inputDTO로_테스트() {
        InputDTO inputDTO = new InputDTO("[콜라-500],[사이다-500]");
        OrderDTO orderDTO = orderService.getOrderFromInput(inputDTO, productDTO);
        assertThat(orderDTO.getOrders()).hasSize(2);
        Order expectedOrder = new Order("콜라", 500);
        assertThat(orderDTO.getOrders().getFirst()).isEqualTo(expectedOrder);
    }

    @Test
    void comma_구분테스트() {
        InputDTO inputDTO = new InputDTO("[콜라-500],[사이다-500]");
        String[] tokens = orderService.splitInputWithComma(inputDTO);
        assertThat(tokens).containsExactly("[콜라-500]", "[사이다-500]");
    }

    @Test
    void par_구분테스트() {
        String[] tokens = {"[콜라-500]", "[사이다-500]"};
        List<String> findObjs = orderService.splitTokenWithPar(tokens);
        assertThat(findObjs).containsExactly("콜라-500", "사이다-500");
    }

    @Test
    void minus_구분테스트() {
        List<String> findObjs = List.of("콜라-500", "사이다-500");
        List<Order> orders = orderService.splitPart(findObjs, productDTO);
        assertThat(orders).hasSize(2);
        Order expectedOrder = new Order("콜라", 500);
        assertThat(orders.getFirst()).isEqualTo(expectedOrder);
    }

    @Test
    void 주문만들기_테스트() {
        List<Order> orders = new ArrayList<>();
        String[] splitObj = {"콜라", "500"};
        orderService.makeOrder(splitObj, orders, productDTO);
        assertThat(orders).hasSize(1);
        Order expectedOrder = new Order("콜라", 500);
        assertThat(orders.getFirst()).isEqualTo(expectedOrder);
    }

    @Test
    void 주문확인_테스트() {
        assertThatCode(() -> orderService.certifyOrderName(productDTO, "콜라"))
                .doesNotThrowAnyException();
    }

    @Test
    void 상품_존재_확인_테스트() {
        assertThatThrownBy(() -> orderService.certifyOrderName(productDTO, "주스"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @Test
    void 충분한_재고량_확인_테스트() {
        assertThatCode(() -> orderService.certifyOrderQuantity(productDTO, 500))
                .doesNotThrowAnyException();
    }

    @Test
    void 재고량_확인_테스트() {
        assertThatThrownBy(() -> orderService.certifyOrderQuantity(productDTO, 2000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    @Test
    void 파싱_테스트() {
        int quantity = orderService.parseOrderQuantity("500");
        assertThat(quantity).isEqualTo(500);
    }

    @Test
    void 주문_입력_테스트() {
        assertThatThrownBy(() -> orderService.parseOrderQuantity("오백"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }

    @Test
    void 토큰_확인_테스트() {
        assertThatCode(() -> orderService.certifyToken("[콜라-500]"))
                .doesNotThrowAnyException();
    }


    @Test
    void 올바른_형식_테스트() {
        assertThatThrownBy(() -> orderService.certifyToken("[콜라-500"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @Test
    void order_내용물_추출_ㅌ스트() {
        String content = orderService.extractContent("[콜라-500]");
        assertThat(content).isEqualTo("콜라-500");
    }
}

