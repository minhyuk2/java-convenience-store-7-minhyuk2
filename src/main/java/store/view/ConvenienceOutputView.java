package store.view;

import store.domain.Product;
import store.domain.dto.ProductDTO;

import java.util.List;

//파일 입출력으로 가져와서 출력시켜주는 부분 필요함
public class ConvenienceOutputView {
    public void printWelcomeMessage(){
        System.out.println("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.\n");
    }

    public void printNowProducts(ProductDTO productDTO){
        List<Product> productList = productDTO.getProducts();
        for(Product product : productList){
            System.out.println(product.getFormattedOutput());
        }
    }

}
