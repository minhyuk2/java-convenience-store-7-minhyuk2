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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BuyController {

    private final ConvenienceInputView convenienceInputView;
    private final ConvenienceOutputView convenienceOutputView;
    private final OrderService orderService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final BuyService buyService;

    public BuyController(ConvenienceInputView convenienceInputView, ConvenienceOutputView convenienceOutputView, OrderService orderService, ProductService productService, PromotionService promotionService, BuyService buyService) {
        this.convenienceInputView = convenienceInputView;
        this.convenienceOutputView = convenienceOutputView;
        this.orderService = orderService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.buyService = buyService;
    }

    //절대경로가 아닌 다른 방법을 사용할 수 있다고 고려
    private final FilePathDTO filePathDTO = new FilePathDTO("src/main/resources/products.md");
    private final FilePathDTO promotionFilePathDTO = new FilePathDTO("src/main/resources/promotions.md");

    public void startConvenienceStore() {
        ProductDTO productDTO = productService.getProducts(filePathDTO);
        PromotionDTO promotionDTO = promotionService.getDataToPromotionDTO(promotionFilePathDTO);
        whileGetOrderAndOthers(productDTO, promotionDTO);
    }

    public void whileGetOrderAndOthers(ProductDTO productDTO, PromotionDTO promotionDTO) {
        do {
            convenienceOutputView.printWelcomeMessage();
            OrderDTO orderDTO = whileGetValidOrder(productDTO);
            catchBuyService(orderDTO, promotionDTO, productDTO);
        } while (!convenienceInputView.inputMoreProduct().getInput().equals("N"));
    }

    public OrderDTO getValidOrderDTO(ProductDTO productDTO, OrderDTO orderDTO) {
        try {
            InputDTO inputDTO = getInputConvenienceInService(productDTO);
            orderDTO = orderService.getOrderFromInput(inputDTO, productDTO);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return orderDTO;
    }

    public OrderDTO whileGetValidOrder(ProductDTO productDTO) {
        OrderDTO orderDTO = null;
        while (orderDTO == null) {
            orderDTO = getValidOrderDTO(productDTO, orderDTO);
        }
        return orderDTO;
    }

    public InputDTO getInputConvenienceInService(ProductDTO productDTO) {
        convenienceOutputView.printNowProducts(productDTO);
        return convenienceInputView.inputProductAndQuantity();
    }

    public void catchBuyService(OrderDTO orderDTO, PromotionDTO promotionDTO, ProductDTO productDTO) {
        try {
            Receipt receipt = buyService.specifyPromotion(orderDTO, promotionDTO, productDTO);
            convenienceOutputView.printReceipt(receipt);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


}
