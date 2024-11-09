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

    public void printPromotionOk(Product product,int promotionQuantity){
        System.out.println("현재 "+ product.getProductName()+"은(는) "+promotionQuantity+"개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void printNoPromotion(Product product,int promotionQuantity){
        System.out.println("현재 "+product.getProductName()+" "+promotionQuantity+"개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }


}
