package store.controller;

import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.domain.dto.*;
import store.service.BuyService;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.ConvenienceInputView;
import store.view.ConvenienceOutputView;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BuyController {

    private final ConvenienceInputView inputView;
    private final ConvenienceOutputView outputView;
    private final OrderService orderService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final BuyService buyService;

    public BuyController(ConvenienceInputView inputView, ConvenienceOutputView outputView, OrderService orderService, ProductService productService, PromotionService promotionService, BuyService buyService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.orderService = orderService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.buyService = buyService;
    }

    private final FilePathDTO productFilePath = new FilePathDTO("src/main/resources/products.md");
    private final FilePathDTO promotionFilePath = new FilePathDTO("src/main/resources/promotions.md");

    public void startConvenienceStore() {
        ProductDTO productDTO = productService.getProducts(productFilePath);
        PromotionDTO promotionDTO = promotionService.getDataToPromotionDTO(promotionFilePath);
        processOrders(productDTO, promotionDTO);
    }

    private void processOrders(ProductDTO productDTO, PromotionDTO promotionDTO) {
        do {
            outputView.printWelcomeMessage();
            OrderDTO orderDTO = getValidOrderDTO(productDTO);
            handlePurchase(orderDTO, promotionDTO, productDTO);
        } while (!inputView.inputMoreProduct().getInput().equals("N"));
    }

    private void handlePurchase(OrderDTO orderDTO, PromotionDTO promotionDTO, ProductDTO productDTO) {
        try {
            Receipt receipt = processReceipt(orderDTO, promotionDTO, productDTO);
            buyService.applyMembershipDiscountIfRequested(receipt, isUserMember());
            receipt.calculateFinalAmount();
            outputView.printReceipt(receipt);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private OrderDTO getValidOrderDTO(ProductDTO productDTO) {
        OrderDTO orderDTO = null;
        while (orderDTO == null) {
            orderDTO = attemptToGetOrderDTO(productDTO);
        }
        return orderDTO;
    }

    private OrderDTO attemptToGetOrderDTO(ProductDTO productDTO) {
        try {
            InputDTO inputDTO = getInputConvenienceInService(productDTO);
            return orderService.getOrderFromInput(inputDTO, productDTO);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public InputDTO getInputConvenienceInService(ProductDTO productDTO) {
        outputView.printNowProducts(productDTO);
        return inputView.inputProductAndQuantity();
    }


    public Receipt processReceipt(OrderDTO orderDTO, PromotionDTO promotionDTO, ProductDTO productDTO) {
        Receipt receipt = new Receipt();
        for (Order order : orderDTO.getOrders()) {
            processOrder(order, promotionDTO, productDTO, receipt);
        }
        return receipt;
    }

    private void processOrder(Order order, PromotionDTO promotionDTO, ProductDTO productDTO, Receipt receipt) {
        Product product = order.findMatchingProduct(productDTO.getProducts());
        Optional<Promotion> promotion = buyService.getApplicablePromotion(product, promotionDTO.getPromotions());
        if (promotion.isPresent()) {
            handlePromotion(order, product, promotion.get(), receipt);
            return;
        }
        buyService.handleNoPromotion(product, order, receipt);
    }

    private void handlePromotion(Order order, Product product, Promotion promotion, Receipt receipt) {
        int[] prepares = makeIntArray(order,product,promotion);
        AtomicInteger freeUnits = new AtomicInteger(prepares[0] * promotion.getGet());
        AtomicInteger totalPromoStockUsed = new AtomicInteger(prepares[0] * (promotion.getBuy() + promotion.getGet()) + prepares[1]);
        boolean checkingOut = buyService.checkForAdditionalPromotion(order, product, prepares[1], promotion, prepares[0], freeUnits, totalPromoStockUsed);
        if (checkingOut) {handleAdditionalPromotion(product, promotion, totalPromoStockUsed, freeUnits);}
        buyService.manageStock(totalPromoStockUsed, product, receipt, calculateNonPromoNormalUnits(order, promotion, prepares[0], prepares[1]));
        if(!processNonPromoUnits(product, prepares[1], calculateNonPromoNormalUnits(order, promotion, prepares[0], prepares[1]),checkingOut, receipt, promotion, freeUnits, prepares[0]))return;
        buyService.finalizePurchase(receipt, product, prepares[0], promotion,prepares[1], calculateNonPromoNormalUnits(order, promotion, prepares[0], prepares[1]), freeUnits);
    }

    private int[] makeIntArray(Order order, Product product ,Promotion promotion){
        int applicableSets = calculateApplicablePromoSets(order, product, promotion);
        int nonPromoUnits = calculateNonPromoUnits(order, product, promotion, applicableSets);
        return new int[]{applicableSets, nonPromoUnits};
    }

    public int calculateApplicablePromoSets(Order order, Product product, Promotion promotion) {
        int promoStockSets = product.getProductPromotionQuantity() / (promotion.getBuy() + promotion.getGet());
        int orderSets = order.getQuantity() / (promotion.getBuy() + promotion.getGet());
        return Math.min(promoStockSets, orderSets);
    }

    public int calculateNonPromoUnits(Order order, Product product, Promotion promotion, int applicableSets) {
        int unitsAfterSets = order.getQuantity() - applicableSets * (promotion.getBuy() + promotion.getGet());
        int remainingPromoStock = product.getProductPromotionQuantity()-applicableSets * (promotion.getBuy() + promotion.getGet());
        return Math.min(unitsAfterSets, remainingPromoStock);
    }

    private int calculateNonPromoNormalUnits(Order order, Promotion promotion, int applicableSets, int nonPromoUnits) {
        return order.getQuantity() - applicableSets * (promotion.getBuy() + promotion.getGet()) - nonPromoUnits;
    }

    private void handleAdditionalPromotion(Product product, Promotion promotion, AtomicInteger totalPromoStockUsed, AtomicInteger freeUnits) {
        outputView.printPromotionOk(product, promotion.getGet());
        if (inputView.inputPromotion().getInput().equalsIgnoreCase("Y")) {
            totalPromoStockUsed.addAndGet(promotion.getGet());
            freeUnits.addAndGet(promotion.getGet());
        }
    }

    private boolean processNonPromoUnits(Product product, int nonPromoUnits, int nonPromoNormalUnits, boolean needMorePromo, Receipt receipt, Promotion promotion, AtomicInteger freeUnits, int applicableSets) {
        int nonPromoTotalUnits = nonPromoUnits + nonPromoNormalUnits;
        if (nonPromoTotalUnits > 0 && !needMorePromo && freeUnits.get() > 0)
            return handleUserDecision(product, nonPromoTotalUnits, nonPromoUnits,nonPromoNormalUnits, receipt, promotion, freeUnits, applicableSets);
        return true;
    }

    private boolean handleUserDecision(Product product, int nonPromoTotalUnits, int nonPromoUnits, int nonPromoNormalUnits, Receipt receipt, Promotion promotion, AtomicInteger freeUnits, int applicableSets) {
        outputView.printNoPromotion(product, nonPromoTotalUnits);
        if (inputView.inputPromotion().getInput().equalsIgnoreCase("N")) {
            restoreStock(product, nonPromoUnits, nonPromoNormalUnits);
            buyService.finalizePurchaseWithoutPromotion(receipt, product, applicableSets,promotion, nonPromoUnits, 0, freeUnits);
            return false;
        }
        return true;
    }

    private void restoreStock(Product product, int nonPromoUnits, int nonPromoNormalUnits) {
        product.restorePromotionQuantity(nonPromoUnits);
        product.restoreStock(nonPromoNormalUnits);
    }

    private boolean isUserMember() {
        return inputView.inputMemberShip().getInput().equalsIgnoreCase("Y");
    }
}

