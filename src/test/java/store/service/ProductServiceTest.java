package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.FilePathDTO;
import store.domain.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;
    private FilePathDTO validFilePath;
    private FilePathDTO invalidFilePath;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        validFilePath = new FilePathDTO("src/test/resources/validProducts.md");
        invalidFilePath = new FilePathDTO("src/main/resources/invalidProducts.md");
    }

    @Test
    void 잘못된_파일_경로_입력() {
        assertThatThrownBy(() -> productService.getLinesFromFile(invalidFilePath.getFileName(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 파일 경로입니다.");
    }

    @Test
    void 문장_자르기_테스트() {
        List<String> lines = List.of("콜라,1000,10,탄산2+1", "사이다,1000,8,탄산2+1");
        List<String[]> tokens = productService.sliceLines(lines);
        assertThat(tokens).hasSize(2).allMatch(arr -> arr.length == 4);
    }

    @Test
    void parse_테스트() {
        assertThat(productService.parseInteger("1000")).isEqualTo(1000);
    }

    @Test
    void 문자가_입려된_테스트() {
        assertThatThrownBy(() -> productService.parseInteger("천원"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 숫자로 변환할 수 없습니다.");
    }

    @Test
    void ProductService의_전체테스트_중_잘못된_경로_테스트() {
       assertThatThrownBy(()-> productService.getProducts(validFilePath))
               .isInstanceOf(IllegalArgumentException.class);
    }
}