package store.view;

import store.domain.dto.InputDTO;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class ConvenienceInputView {
    public InputDTO inputProductAndQuantity(){
        System.out.println("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return new InputDTO(readLine());
    }

    public InputDTO inputPromotion(){
        return new InputDTO(getValidYNInput());
    }

    public InputDTO inputMemberShip(){
        System.out.println("\n멤버십 할인을 받으시겠습니까? (Y/N)");
        return new InputDTO(getValidYNInput());
    }
    public InputDTO inputMoreProduct(){
        System.out.println("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return new InputDTO(getValidYNInput());
    }

    private String getValidYNInput() {
        while (true) {
            String input = catchERROR(getReadLine());
            if ("Y".equals(input) || "N".equals(input)) {
                return input;
            }
        }
    }

    private String getReadLine(){
        return readLine().toUpperCase().trim();
    }
    private String catchERROR(String input) {
        try {
            if (!("Y".equals(input) || "N".equals(input))) {
                throw new IllegalArgumentException("[ERROR] Y 또는 N만 입력 가능합니다. 다시 입력해 주세요.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return input;
    }



}
