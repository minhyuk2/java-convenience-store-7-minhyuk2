package store.domain;

import java.util.List;

public class ProductDTO {
    private List<Product> products;

    public ProductDTO(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
