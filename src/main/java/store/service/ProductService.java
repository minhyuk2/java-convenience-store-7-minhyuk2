package store.service;

import store.domain.dto.FilePathDTO;
import store.domain.Product;
import store.domain.dto.ProductDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//파일 안에 데이터가 없거나 형식이 잘못되었을 경우에 대한 에외처리를 진행해야한다.
//이 부분에 대한 테스트도 생성해야한다.

public class ProductService {


    //일단 모든 문장을 저장해서 불러오기
    public void getLinesFromFile(String filePath, List<String> lines) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            skipHeader(br);
            readLines(br, lines);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 파일 경로입니다.");
        }
    }

    //첫 한 줄을 제거하기 위함
    private void skipHeader(BufferedReader br) throws IOException {
        br.readLine();
    }

    private void readLines(BufferedReader br, List<String> lines) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
    }

    //토큰으로 문장 나누기
   public List<String[]> sliceLines(List<String> lines) {
        List<String[]> tokens = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            String[] token = line.split(",");
            tokens.add(token);
        }
        return tokens;
    }

    //null도 그냥 문자열로 넘어와있는 상태가 되는 것이다.
    //불러온 문장에서 필요한 정보를 빼오기
    public List<Product> getDataFromList(List<String[]> tokens) {
        List<Product> products = new ArrayList<>();
        try {
            products = makeProductList(tokens, products);
        } catch (Exception e) {
           throw new IllegalArgumentException(e.getMessage());
        }
        return products;
    }

    //공백에 대한 예외처리 고려해야함
    public List<Product> makeProductList(List<String[]> tokens, List<Product> products) {
        for (String[] token : tokens) {
            updateOrAddProduct(products,token);
        }
        return products;
    }
    private void updateOrAddProduct( List<Product> products,String[] token) {
        Product existingProduct = findProductByName(products, token[0]);
        if (existingProduct != null) {
            existingProduct.addProductQuantity(parseInteger(token[1]));
            return;
        }
        products.add(new Product(token[0], parseInteger(token[1]), parseInteger(token[2]), token[3]));
    }

    //이미 존재하는게 있는 경우에는 한 개로 묶기
    private Product findProductByName(List<Product> products, String name) {
        for (Product product : products) {
           if(product.equalName(name)){
               return product;
           }
        }
        return null; // 조건을 만족하는 제품이 없는 경우
    }


    public int parseInteger(String token) {
        try {
            return Integer.parseInt(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 숫자로 변환할 수 없습니다.");
        }
    }

    //여기서 발생되는 exception들 전부 처리하는 식으로 진행하는게 좋아보임
    //뺴온 정보들 리턴하는 함수 만들기
    //이게 모든 것을 만드는 함수다.
    public ProductDTO getProducts(FilePathDTO filePathDTO) {
        List<String> lines = new ArrayList<>();
        getLinesFromFile(filePathDTO.getFileName(), lines);
        return new ProductDTO(getDataFromList(sliceLines(lines))); //이러면 에러를 처리하기 어렵기에 지울 생각해야한다.
    }

}
