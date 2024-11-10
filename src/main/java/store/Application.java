package store;

import store.controller.BuyController;
import store.service.BuyService;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.ConvenienceInputView;
import store.view.ConvenienceOutputView;

public class Application {
    public static void main(String[] args) {
        ConvenienceOutputView convenienceOutputView = new ConvenienceOutputView();
        ConvenienceInputView  convenienceInputView = new ConvenienceInputView();
        OrderService orderService = new OrderService();
        ProductService productService = new ProductService();
        PromotionService promotionService = new PromotionService(productService);
        BuyService buyService = new BuyService();
        BuyController buyController = new BuyController(convenienceInputView,convenienceOutputView,orderService,productService,promotionService,buyService);
        buyController.startConvenienceStore();
    }
}
