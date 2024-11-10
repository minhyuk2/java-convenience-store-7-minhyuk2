package store.service;

import store.domain.*;
import store.domain.dto.InputDTO;
import store.domain.dto.OrderDTO;
import store.domain.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public OrderDTO catchErrorOrder(InputDTO inputDTO, ProductDTO productDTO) {
        OrderDTO orderDTO = new OrderDTO(new ArrayList<>());
        try {
            getOrderFromInput(inputDTO, productDTO);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return orderDTO;
    }

    public OrderDTO getOrderFromInput(InputDTO inputDTO, ProductDTO productDTO) {
        String[] tokens = splitInputWithComma(inputDTO);
        List<String> findObjs = splitTokenWithPar(tokens);
        return new OrderDTO(splitPart(findObjs, productDTO));
    }

    public String[] splitInputWithComma(InputDTO inputDTO) {
        String inputLine = inputDTO.getInput();
        inputLine = inputLine.trim();
        return inputLine.split(",");
    }

    public List<String> splitTokenWithPar(String[] tokens) {
        List<String> findObjs = new ArrayList<>();
        for (String token : tokens) {
            certifyToken(token.trim());
            token = extractContent(token);
            findObjs.add(token);
        }
        return findObjs;
    }

    public List<Order> splitPart(List<String> findObjs, ProductDTO productDTO) {
        List<Order> orders = new ArrayList<>();
        for (String obj : findObjs) {
            String[] splitObj = obj.split("-", 2);
            makeOrder(splitObj, orders, productDTO);
        }
        return orders;
    }

    public void makeOrder(String[] splitObj, List<Order> orders, ProductDTO productDTO) {
        certifyOrderName(productDTO, splitObj[0]);
        int orderQuantity = parseOrderQuantity(splitObj[1]);
        certifyOrderQuantity(productDTO, orderQuantity);
        orders.add(new Order(splitObj[0], orderQuantity));
    }

    public void certifyOrderName(ProductDTO productDTO, String orderObject) {
        if (productDTO.getProducts().stream().noneMatch(product -> product.equalName(orderObject))) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    public void certifyOrderQuantity(ProductDTO productDTO, int orderQuantity) {
        if (productDTO.getProducts().stream().noneMatch(product -> product.hasSufficientQuantity(orderQuantity))) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public int parseOrderQuantity(String orderQuantity) {
        int orderQuantityCount;
        try {
            orderQuantityCount = Integer.parseInt(orderQuantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
        return orderQuantityCount;
    }

    public void certifyToken(String token) {
        if (!token.startsWith("[")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (!token.endsWith("]")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    public String extractContent(String token) {
        return token.substring(1, token.length() - 1);
    }
}
