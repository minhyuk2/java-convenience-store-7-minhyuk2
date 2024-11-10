package store.service;

import store.domain.Product;
import store.domain.Promotion;
import store.domain.dto.FilePathDTO;
import store.domain.dto.PromotionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {


    private final ProductService productService;

    public PromotionService(ProductService productService) {
        this.productService = productService;
    }

    public PromotionDTO getDataToPromotionDTO(FilePathDTO filePathDTO){
        List<String> lines = new ArrayList<>();
        productService.getLinesFromFile(filePathDTO.getFileName(), lines);
        return new PromotionDTO(getDataFromList(productService.sliceLines(lines)));
    }

    public List<Promotion> getDataFromList(List<String[]> tokens) {
        List<Promotion> promotions = new ArrayList<>();
        try {
            promotions = makePromotionList(tokens, promotions);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return promotions;
    }

    public List<Promotion> makePromotionList(List<String[]> tokens, List<Promotion> promotions) throws Exception {
        for (String[] token : tokens) {
            promotions.add(makePromotion(token));
        }
        return promotions;
    }

    public Promotion makePromotion(String[] token) {
        return new Promotion(token[0],parseInteger(token[1]),parseInteger(token[2]),parseDate(token[3]),parseDate(token[4]));
    }

    private int parseInteger(String token) {
        try {
            return Integer.parseInt(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 숫자로 변환할 수 없습니다.");
        }
    }

    private LocalDate parseDate(String token) {
        try{
            return LocalDate.parse(token);
        }catch (Exception e){
            throw new IllegalArgumentException("[ERROR] 날짜 형식으로 변환할 수 없습니다.");
        }
    }

}
