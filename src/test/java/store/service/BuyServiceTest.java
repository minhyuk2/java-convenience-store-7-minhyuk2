package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.*;
import store.domain.dto.OrderDTO;
import store.domain.dto.ProductDTO;
import store.domain.dto.PromotionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

class BuyServiceTest {

    private BuyService buyService;

    @BeforeEach
    void setUp() {
        buyService = new BuyService();
    }

    @Test
    void 가능한_프로모션_찾는_테스트() {
        Product product = new Product("Test Product", 1000, 5, "Promo1");
        Promotion promotion = new Promotion("Promo1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        List<Promotion> promotions = List.of(promotion);
        Optional<Promotion> applicablePromotion = buyService.getApplicablePromotion(product, promotions);
        assertThat(applicablePromotion).isPresent();
        assertThat(applicablePromotion.get()).isEqualTo(promotion);
    }


}
