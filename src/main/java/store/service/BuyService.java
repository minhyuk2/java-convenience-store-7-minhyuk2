package store.service;

import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.domain.dto.OrderDTO;
import store.domain.dto.ProductDTO;
import store.domain.dto.InputDTO;
import store.domain.dto.PromotionDTO;
import store.view.ConvenienceInputView;
import store.view.ConvenienceOutputView;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BuyService {

    private final ConvenienceInputView inputView = new ConvenienceInputView();
    private final ConvenienceOutputView outputView = new ConvenienceOutputView();


    public Optional<Promotion> getApplicablePromotion(Product product, List<Promotion> promotions) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(product.getProductPromotion()))
                .filter(Promotion::getIsNowOk)
                .findFirst();
    }

    public void specifyPromotion(OrderDTO orderDTO, PromotionDTO promotionDTO, ProductDTO productDTO) {
        Receipt receipt = new Receipt();
        orderDTO.getOrders().forEach(order -> processOrder(order, productDTO, promotionDTO.getPromotions(),receipt));
        applyMembershipDiscountIfRequested(receipt);
        receipt.calculateFinalAmount();
        outputView.printReceipt(receipt);
    }

    private void processOrder(Order order, ProductDTO productDTO, List<Promotion> promotions,Receipt receipt) {
        Product product = findProduct(order, productDTO);
        Optional<Promotion> promotion = getApplicablePromotion(product, promotions);
        promotion.ifPresentOrElse(
                promo -> applyPromotions(order, product, promo,receipt),
                () -> handleNoPromotion(product, order,receipt)
        );
    }

    private Product findProduct(Order order, ProductDTO productDTO) {
        return order.findMatchingProduct(productDTO.getProducts());
    }

    private void addPurchasedItemToReceipt(Product product, Order order,Receipt receipt) {
        receipt.addPurchasedItem(product.getProductName(), order.getQuantity(), product.getProductPrice());
    }


    public void handleNoPromotion(Product product, Order order,Receipt receipt) {
        if (!product.hasSufficientQuantity(order.getQuantity()))
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다.");
        product.reduceQuantity(order.getQuantity(), false);
        addPurchasedItemToReceipt(product, order,receipt);
    }

    private void applyMembershipDiscountIfRequested(Receipt receipt) {
        boolean isMembership = inputView.inputMemberShip().getInput().equalsIgnoreCase("Y");
        if (isMembership)
            applyMembershipDiscount(receipt);
    }

    //멤버쉽은 프로모션이 적용된 상품에게는 적용되지 않는다.
    private void applyMembershipDiscount(Receipt receipt) {
        int nonPromoAmount = receipt.getTotalPurchaseAmount() - receipt.getPromotionAmount();
        int discount = nonPromoAmount * 30 / 100;
        receipt.applyMembershipDiscount(discount);
    }

    private void applyPromotions(Order order, Product product, Promotion promotion,Receipt receipt) {
        int applicablePromoSets = Math.min((product.getProductPromotionQuantity() / ( promotion.getBuy() + promotion.getGet())), (order.getQuantity() / ( promotion.getBuy() + promotion.getGet())));
        AtomicInteger freeUnits = new AtomicInteger(applicablePromoSets * promotion.getGet());
        int nonPromoUnitsFromPromoStock = Math.min((order.getQuantity() - (applicablePromoSets * ( promotion.getBuy() + promotion.getGet()))), (product.getProductPromotionQuantity() - (applicablePromoSets * ( promotion.getBuy() + promotion.getGet()))));
        AtomicInteger totalPromoStockUsed = new AtomicInteger((applicablePromoSets * ( promotion.getBuy() + promotion.getGet())) + nonPromoUnitsFromPromoStock);
        boolean comeGetPromotion = findExceptionAndMoreProMo(order,product,nonPromoUnitsFromPromoStock,promotion,applicablePromoSets,freeUnits,totalPromoStockUsed);
        manageStock(totalPromoStockUsed,product,receipt,((order.getQuantity() - (applicablePromoSets * ( promotion.getBuy() + promotion.getGet()))) - nonPromoUnitsFromPromoStock));
        if(!assignBuyNoPromotion(product,nonPromoUnitsFromPromoStock,((order.getQuantity() - (applicablePromoSets * ( promotion.getBuy() + promotion.getGet()))) - nonPromoUnitsFromPromoStock),comeGetPromotion,receipt,promotion,freeUnits,applicablePromoSets)) return ;
        endTotal(receipt,product,applicablePromoSets,promotion,nonPromoUnitsFromPromoStock,((order.getQuantity() - (applicablePromoSets * ( promotion.getBuy() + promotion.getGet()))) - nonPromoUnitsFromPromoStock),freeUnits);
    }

    public boolean findExceptionAndMoreProMo(Order order,Product product,int nonPromoUnitsFromPromoStock,Promotion promotion,int applicablePromoSets,AtomicInteger freeUnits,AtomicInteger totalPromoStockUsed) {
        foundException(order,product);
        return morePromotions(nonPromoUnitsFromPromoStock,promotion,product,(product.getProductPromotionQuantity() - (applicablePromoSets * ( promotion.getBuy() + promotion.getGet()))), promotion.getBuy() + promotion.getGet(),totalPromoStockUsed,freeUnits);
    }


    public void manageStock(AtomicInteger totalPromoStockUsed,Product product,Receipt receipt ,int nonPromoUnitsFromNormalStock){
        // 프로모션 재고 차감
        promotionStock(totalPromoStockUsed.get(),product,receipt);
        //일반 재고 소모
        normalStock(product,nonPromoUnitsFromNormalStock);
    }


    public void endTotal(Receipt receipt,Product product,int applicablePromoSets, Promotion promotion,int nonPromoUnitsFromPromoStock,int nonPromoUnitsFromNormalStock,AtomicInteger freeUnits){
        writeReceipt(receipt,product,applicablePromoSets * promotion.getBuy(),nonPromoUnitsFromPromoStock,nonPromoUnitsFromNormalStock,freeUnits.get());
        addFreeItem(product,freeUnits.get(),receipt);
        if(freeUnits.get() == 0){
            receipt.addPromotionAmount(-(nonPromoUnitsFromPromoStock*product.getProductPrice()));
        }
    }

    public void endTotalNoPro(Receipt receipt,Product product,int applicablePromoSets, Promotion promotion,int nonPromoUnitsFromPromoStock,int nonPromoUnitsFromNormalStock,AtomicInteger freeUnits){
        writeReceipt(receipt,product,applicablePromoSets * promotion.getBuy(),0,nonPromoUnitsFromNormalStock,freeUnits.get());
        addFreeItem(product,freeUnits.get(),receipt);
        receipt.addPromotionAmount(-(nonPromoUnitsFromPromoStock*product.getProductPrice()));
    }


    public boolean morePromotions(int nonPromoUnitsFromPromoStock, Promotion promotion, Product product, int remainingPromoStock, int promotionUnit, AtomicInteger totalPromoStockUsed, AtomicInteger freeUnits){
        if((nonPromoUnitsFromPromoStock == promotion.getBuy())&&(remainingPromoStock >= promotion.getGet())&&(remainingPromoStock >= promotionUnit)){
            outputView.printPromotionOk(product,promotion.getGet());
            return morePromotionsInput(totalPromoStockUsed,freeUnits,promotion);
        }
        return false;
    }


    public boolean morePromotionsInput(AtomicInteger totalPromoStockUsed,AtomicInteger freeUnits,Promotion promotion){
        if(inputView.inputPromotion().getInput().equalsIgnoreCase("Y")){
            totalPromoStockUsed.set(totalPromoStockUsed.get()+promotion.getGet()) ;
            freeUnits.set(freeUnits.get()+promotion.getGet());
            return true;
        }
        return false;
    }

    public void foundException(Order order, Product product){
        if(order.getQuantity() > product.getProductPromotionQuantity()+ product.getProductQuantity()){
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

    }

    public void promotionStock(int totalPromoStockUsed, Product product ,Receipt receipt) {
        if (totalPromoStockUsed > 0) {
            product.reduceQuantity(totalPromoStockUsed, true);
            receipt.addPromotionAmount(totalPromoStockUsed*product.getProductPrice());
        }
    }

    public void normalStock(Product product,int nonPromoUnitsFromNormalStock){
        // 일반 재고 차감 프로모션 재고가 모자른 경우
        if (nonPromoUnitsFromNormalStock > 0) {
            if (product.hasSufficientQuantity(nonPromoUnitsFromNormalStock)) {
                product.reduceQuantity(nonPromoUnitsFromNormalStock, false);
            }
        }
    }

    public void writeReceipt(Receipt receipt,Product product,int promoUnitsRequired, int nonPromoUnitsFromPromoStock,int nonPromoUnitsFromNormalStock,int freeUnits) {
        int totalPaidUnits = promoUnitsRequired + nonPromoUnitsFromPromoStock + nonPromoUnitsFromNormalStock + freeUnits;
        receipt.addPurchasedItem(product.getProductName(), totalPaidUnits, product.getProductPrice());
    }

    public void addFreeItem(Product product, int freeUnits,Receipt receipt) {
        if (freeUnits > 0) {
            receipt.addFreeItem(product.getProductName(), freeUnits);
            int discountAmount = freeUnits * product.getProductPrice();
            receipt.addPromotionDiscount(discountAmount);
        }
    }

    //이 부분을 어떻게 controller로 뺄 수 있을까 고민
    public boolean buyNoPromotion(Product product, int nonPromoTotalUnits, int nonPromoUnitsFromPromoStock,int nonPromoUnitsFromNormalStock,Receipt receipt,Promotion promotion,AtomicInteger freeUnits,int applicablePromoSets) {
        outputView.printNoPromotion(product, nonPromoTotalUnits);
        if (inputView.inputPromotion().getInput().equalsIgnoreCase("N")) {
            product.restorePromotionQuantity(nonPromoUnitsFromPromoStock);
            product.restoreStock(nonPromoUnitsFromNormalStock);
            endTotalNoPro(receipt,product,applicablePromoSets,promotion,nonPromoUnitsFromPromoStock,0,freeUnits);
            return false;
        }
        return true;
    }

    public boolean assignBuyNoPromotion(Product product, int nonPromoUnitsFromPromoStock,int nonPromoUnitsFromNormalStock,boolean comeGetPromotion,Receipt receipt,Promotion promotion,AtomicInteger freeUnits,int applicablePromoSets) {
        int nonPromoTotalUnits = nonPromoUnitsFromPromoStock + nonPromoUnitsFromNormalStock;
        if (nonPromoTotalUnits > 0 && !comeGetPromotion && freeUnits.get()>0) {
            return buyNoPromotion(product, nonPromoTotalUnits, nonPromoUnitsFromPromoStock, nonPromoUnitsFromNormalStock,receipt, promotion,freeUnits,applicablePromoSets);
        }
        return true;
    }

}
