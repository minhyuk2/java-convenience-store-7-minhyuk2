package store.domain.dto;

import store.domain.Promotion;

import java.util.List;

public class PromotionDTO {
    private List<Promotion> promotions;

    public PromotionDTO(List<Promotion> promotions) {
        this.promotions = promotions;
    }

}
