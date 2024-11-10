package store.view;

import store.domain.Product;
import store.domain.Receipt;
import store.domain.dto.ProductDTO;

import java.util.List;

public class ConvenienceOutputView {
    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.\n");
    }

    public void printNowProducts(ProductDTO productDTO) {
        List<Product> productList = productDTO.getProducts();
        for (Product product : productList) {
            System.out.println(product.getFormattedOutput());
        }
    }

    public void printPromotionOk(Product product, int promotionQuantity) {
        System.out.println("\n현재 " + product.getProductName() + "은(는) " + promotionQuantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void printNoPromotion(Product product, int promotionQuantity) {
        System.out.println("\n현재 " + product.getProductName() + " " + promotionQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    public void printReceipt(Receipt receipt) {
        System.out.println();
        System.out.println("==============W 편의점================");
        printItems(receipt);
        System.out.println("=============증      정===============");
        receipt.getFreeItems().forEach(System.out::println);
        System.out.println("====================================");
        printMoney(receipt);
    }

    private void printItems(Receipt receipt) {
        System.out.printf("%-19s%-9s%-8s\n", "상품명", "수량", "금액");
        receipt.getPurchasedItems().forEach(System.out::println);
    }

    private void printMoney(Receipt receipt) {
        System.out.printf("%-19s%-9d%-8s\n", "총구매액", receipt.getTotalQuantity(), String.format("%,d", receipt.getTotalPurchaseAmount()));
        System.out.printf("%-28s%-8s\n", "행사할인", String.format("-%,d", receipt.getTotalPromotionDiscount()));
        System.out.printf("%-27s%-8s\n", "멤버십할인", String.format("-%,d", receipt.getMembershipDiscount()));
        System.out.printf("%-29s%-8s\n", "내실돈", String.format("%,d", receipt.getFinalAmount()));
    }

}
