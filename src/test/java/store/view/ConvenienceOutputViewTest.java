package store.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.FilePathDTO;
import store.domain.Product;
import store.domain.ProductDTO;
import store.service.ConvenienceService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConvenienceOutputViewTest {

    private ConvenienceOutputView convenienceOutputView = new ConvenienceOutputView();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStream() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    void printNowProducts() {
        List<Product> productList = List.of(new Product("가방", 1000, 3, null),new Product("사탕", 500, 2, "추천행사"));
        ProductDTO productDTO = new ProductDTO(productList);
        convenienceOutputView.printNowProducts(productDTO);
        assertThat(outputStream.toString()).isEqualTo("- 가방 1,000원 3개\n- 사탕 500원 2개 추천행사\n");
    }
}