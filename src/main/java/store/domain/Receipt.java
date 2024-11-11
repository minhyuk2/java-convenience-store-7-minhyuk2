package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<String> purchasedItems;
    private final List<String> freeItems;
    private int totalPurchaseAmount;
    private int promotionDiscounts;
    private int membershipDiscount;
    private int finalAmount;
    private int totalQuantity;
    private int promotionAmount;

    public Receipt() {
        this.purchasedItems = new ArrayList<>();
        this.freeItems = new ArrayList<>();
        this.promotionDiscounts = 0;
        this.totalPurchaseAmount = 0;
        this.membershipDiscount = 0;
        this.finalAmount = 0;
        this.totalQuantity = 0;
        this.promotionAmount = 0;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public int getPromotionAmount() {
        return promotionAmount;
    }

    public void addPromotionAmount(int amount) {
        this.promotionAmount += amount;
    }

    public void addPurchasedItem(String productName, int quantity, int price) {
        purchasedItems.add(String.format("%-20s%-9d%-8s", productName, quantity, String.format("%,d", quantity * price)));
        totalPurchaseAmount += quantity * price;
        totalQuantity += quantity;
    }

    public void addFreeItem(String productName, int quantity) {
        freeItems.add(String.format("%-20s%-9d", productName, quantity));
    }

    public void addPromotionDiscount(int discount) {
        promotionDiscounts += discount;
    }

    public void applyMembershipDiscount(int discount) {
        this.membershipDiscount = Math.min(discount, 8000); // 최대 8,000원 할인
    }

    public void calculateFinalAmount() {
        finalAmount = totalPurchaseAmount - promotionDiscounts - membershipDiscount;
    }

    public int getTotalPromotionDiscount() {
        return promotionDiscounts;
    }

    public List<String> getPurchasedItems() {
        return purchasedItems;
    }

    public List<String> getFreeItems(){
        return freeItems;
    }

}
