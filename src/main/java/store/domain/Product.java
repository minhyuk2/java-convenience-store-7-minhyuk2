package store.domain;

public class Product {
    private final String productName;
    private final int productPrice;
    private int productQuantity;
    private final String productPromotion;

    //생성자에서 구분하게 만들 수도 있겠네
    public Product(String productName, int productPrice, int productQuantity, String productPromotion) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productPromotion = productPromotion;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    private String formatPrice(int amount) {
        return String.format("%,d", amount);
    }

    public String getFormattedOutput(){
        if(productPromotion == null){
         return String.format("- " + productName + " " + formatPrice(productPrice) +"원 "+ productQuantity +"개");
        }
        return String.format("- " + productName + " " + formatPrice(productPrice) +"원 "+ productQuantity +"개 " + productPromotion);
    }

}
