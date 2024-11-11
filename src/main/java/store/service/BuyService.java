package store.service;

import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BuyService {

    public Optional<Promotion> getApplicablePromotion(Product product, List<Promotion> promotions) {
        return promotions.stream()
                .filter(promo -> promo.getName().equals(product.getProductPromotion()))
                .filter(Promotion::getIsNowOk)
                .findFirst();
    }

    public void handleNoPromotion(Product product, Order order, Receipt receipt) {
        if (!product.hasSufficientQuantity(order.getQuantity()))
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        product.reduceQuantity(order.getQuantity(), false);
        addPurchasedItemToReceipt(product, order.getQuantity(), receipt);
    }

    private void addPurchasedItemToReceipt(Product product, int quantity, Receipt receipt) {
        receipt.addPurchasedItem(product.getProductName(), quantity, product.getProductPrice());
    }

    public void applyMembershipDiscountIfRequested(Receipt receipt, boolean isMembership) {
        if (isMembership)
            applyMembershipDiscount(receipt);
    }

    public void applyMembershipDiscount(Receipt receipt) {
        int nonPromoAmount = receipt.getTotalPurchaseAmount() - receipt.getPromotionAmount();
        int discount = nonPromoAmount * 30 / 100;
        receipt.applyMembershipDiscount(discount);
    }

    public boolean checkForAdditionalPromotion(Order order, Product product, int nonPromoUnits, Promotion promotion, int applicableSets, AtomicInteger freeUnits, AtomicInteger totalPromoStockUsed) {
        validateStockAvailability(order, product);
        return evaluateAdditionalPromotion(nonPromoUnits, promotion, product, applicableSets, totalPromoStockUsed, freeUnits);
    }

    private void validateStockAvailability(Order order, Product product) {
        if (order.getQuantity() > product.getProductPromotionQuantity() + product.getProductQuantity())
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    public boolean evaluateAdditionalPromotion(int nonPromoUnits, Promotion promotion, Product product, int applicableSets, AtomicInteger totalPromoStockUsed, AtomicInteger freeUnits) {
        int remainingPromoStock = product.getProductPromotionQuantity() - applicableSets * (promotion.getBuy() + promotion.getGet());
        int promoUnit = promotion.getBuy() + promotion.getGet();
        if (nonPromoUnits == promotion.getBuy() && remainingPromoStock >= promoUnit)
            return true;
        return false;
    }

    public void manageStock(AtomicInteger totalPromoStockUsed, Product product, Receipt receipt, int nonPromoNormalUnits) {
        adjustPromotionStock(totalPromoStockUsed.get(), product, receipt);
        adjustNormalStock(product, nonPromoNormalUnits);
    }

    private void adjustPromotionStock(int totalPromoStockUsed, Product product, Receipt receipt) {
        if (totalPromoStockUsed > 0) {
            product.reduceQuantity(totalPromoStockUsed, true);
            receipt.addPromotionAmount(totalPromoStockUsed * product.getProductPrice());
        }
    }

    private void adjustNormalStock(Product product, int nonPromoNormalUnits) {
        if (nonPromoNormalUnits > 0 && product.hasSufficientQuantity(nonPromoNormalUnits)) {
            product.reduceQuantity(nonPromoNormalUnits, false);
        }
    }

    public void finalizePurchase(Receipt receipt, Product product, int applicableSets, Promotion promotion, int nonPromoUnits, int nonPromoNormalUnits, AtomicInteger freeUnits) {
        updateReceipt(receipt, product, applicableSets * promotion.getBuy(), nonPromoUnits, nonPromoNormalUnits, freeUnits.get());
        addFreeItemsToReceipt(product, freeUnits.get(), receipt);
        if (freeUnits.get() == 0)
            receipt.addPromotionAmount(-nonPromoUnits * product.getProductPrice());
    }

    private void updateReceipt(Receipt receipt, Product product, int promoUnits, int nonPromoUnits, int nonPromoNormalUnits, int freeUnits) {
        int totalUnits = promoUnits + nonPromoUnits + nonPromoNormalUnits + freeUnits;
        receipt.addPurchasedItem(product.getProductName(), totalUnits, product.getProductPrice());
    }

    public void addFreeItemsToReceipt(Product product, int freeUnits, Receipt receipt) {
        if (freeUnits > 0) {
            receipt.addFreeItem(product.getProductName(), freeUnits);
            int discount = freeUnits * product.getProductPrice();
            receipt.addPromotionDiscount(discount);
        }
    }

    public void finalizePurchaseWithoutPromotion(Receipt receipt, Product product, int applicableSets, Promotion promotion, int nonPromoUnits, int nonPromoNormalUnits, AtomicInteger freeUnits) {
        updateReceipt(receipt, product, applicableSets * promotion.getBuy(), 0, nonPromoNormalUnits, freeUnits.get());
        addFreeItemsToReceipt(product, freeUnits.get(), receipt);
        receipt.addPromotionAmount(-nonPromoUnits * product.getProductPrice());
    }
}
