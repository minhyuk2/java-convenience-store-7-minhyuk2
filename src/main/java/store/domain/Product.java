package store.domain;

public class Product {
    private final String productName;
    private final int productPrice;
    private int productQuantity;
    private final String productPromotion;
    private int productPromotionQuantity = 0;

    public Product(String productName, int productPrice, int productQuantity, String productPromotion) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productPromotion = productPromotion;
        if (!productPromotion.equals("null")) {
            productPromotionQuantity = productQuantity;
        }
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void addProductQuantity(int productQuantity) {
        this.productQuantity += productQuantity;
    }

    private String formatPrice(int amount) {
        return String.format("%,d", amount);
    }

    public boolean equalName(String objName){
        return productName.equals(objName);
    }
    public boolean hasSufficientQuantity(int orderQuantity) {
        return productQuantity >= orderQuantity;
    }

    public String getFormattedOutput(){
        if(productPromotion == null){
         return String.format("- " + productName + " " + formatPrice(productPrice) +"원 "+ productQuantity +"개");
        }
        return String.format("- " + productName + " " + formatPrice(productPrice) +"원 "+ productQuantity +"개 " + productPromotion);
    }

}
