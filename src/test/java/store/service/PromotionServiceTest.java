package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.domain.dto.FilePathDTO;
import store.domain.dto.PromotionDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PromotionServiceTest {

    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = new PromotionService(new ProductService());
    }

    @Test
    void 전체테스트() {
        FilePathDTO filePathDTO = new FilePathDTO("src/test/java/resources/promotions.md");
        PromotionDTO promotionDTO = promotionService.getDataToPromotionDTO(filePathDTO);
        List<Promotion> promotions = promotionDTO.getPromotions();
        assertThat(promotions).isNotNull();
        assertThat(promotions.size()).isEqualTo(3);
    }

    @Test
    void 리스트에서_데이터_추출() {
        List<String[]> tokens = new ArrayList<>();
        tokens.add(new String[]{"탄산2+1", "2", "1", "2024-01-01", "2024-12-31"});
        List<Promotion> promotions = promotionService.getDataFromList(tokens);
        assertThat(promotions.size()).isEqualTo(1);
    }

    @Test
    void 프로모션_생성_테스트() {
        Promotion promotion = promotionService.makePromotion(new String[]{"반짝할인", "1", "1", "2024-11-01", "2024-11-30"});
        assertThat(promotion).extracting("name", "buy", "get")
                .containsExactly("반짝할인", 1, 1);
    }
}