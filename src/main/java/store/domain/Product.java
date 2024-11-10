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
            this.productPromotionQuantity = productQuantity;
            this.productQuantity = 0;
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
        return productQuantity+productPromotionQuantity >= orderQuantity;
    }

    public String getFormattedOutput(){
        if(productPromotion.equals("null")){
            return String.format("- " + productName + " " + formatPrice(productPrice) +"원 "+ zeroQuantity(productQuantity));
        }
        return String.format("- " + productName + " " + formatPrice(productPrice) +"원 "+ zeroQuantity(productPromotionQuantity) +" "+ productPromotion+"\n"+
                "- " + productName + " " + formatPrice(productPrice) +"원 "+ zeroQuantity(productQuantity));
    }

    private String zeroQuantity(int productQuantitys) {
        if (productQuantitys == 0) {
            return "재고 없음";
        }
        return productQuantitys + "개";
    }



    public String getProductName() {
        return productName;
    }

    public void reduceQuantity(int orderQuantity, boolean isPromotionApplied) {
        if (isPromotionApplied && productPromotionQuantity >= orderQuantity) {
            productPromotionQuantity -= orderQuantity;
            return;
        }
        productQuantity -= orderQuantity;
    }

    public String getProductPromotion (){
        return productPromotion;
    }

    public int getProductQuantity(){
        return productQuantity;
    }

    public int getProductPromotionQuantity(){
        return productPromotionQuantity;
    }

    public int getProductPrice(){
        return productPrice;
    }
    public void restoreStock(int quantity) {
        this.productQuantity += quantity;
    }
    public void restorePromotionQuantity(int promotionQuantity) {
        this.productPromotionQuantity += promotionQuantity;
    }
}
