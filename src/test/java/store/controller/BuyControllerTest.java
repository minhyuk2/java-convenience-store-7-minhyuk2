package store.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.domain.dto.OrderDTO;
import store.domain.dto.ProductDTO;
import store.domain.dto.PromotionDTO;
import store.service.BuyService;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.ConvenienceInputView;
import store.view.ConvenienceOutputView;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BuyControllerTest {
    private BuyController buyController;
    private BuyService buyService;
    private OrderService orderService;
    private ProductService productService;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        buyService = new BuyService();
        orderService = new OrderService();
        productService = new ProductService();
        promotionService = new PromotionService(productService);
        buyController = new BuyController(new ConvenienceInputView(), new ConvenienceOutputView(), orderService, productService, promotionService, buyService);
    }

    @Test
    void 영수증_생성_테스트() {
        OrderDTO orderDTO = new OrderDTO(List.of(new Order("콜라", 3)));
        ProductDTO productDTO = new ProductDTO(List.of(new Product("콜라", 1000, 10, "탄산2+1")));
        PromotionDTO promotionDTO = new PromotionDTO(List.of(new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))));
        Receipt receipt = buyController.processReceipt(orderDTO, promotionDTO, productDTO);
        assertThat(receipt).isNotNull();
        assertThat(receipt.getPurchasedItems()).isNotEmpty();
    }

    @Test
    void 적용_가능한_프로모션_테스트() {
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Order order = new Order("콜라", 5);
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        int sets = buyController.calculateApplicablePromoSets(order, product, promotion);
        assertThat(sets).isEqualTo(1);
    }

    @Test
    void 프로모션_이외의_재고_테스트() {
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Order order = new Order("콜라", 5);
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        int nonPromoUnits = buyController.calculateNonPromoUnits(order, product, promotion, 1);
        assertThat(nonPromoUnits).isEqualTo(2);
    }
}
