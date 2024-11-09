package store.view;

import store.domain.dto.InputDTO;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class ConvenienceInputView {
    public InputDTO inputProductAndQuantity(){
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return new InputDTO(readLine());
    }

    public InputDTO inputPromotion(){
        return new InputDTO(readLine());
    }

    public InputDTO inputMemberShip(){
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return new InputDTO(readLine());
    }
    public InputDTO inputMoreProduct(){
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return new InputDTO(readLine());
    }

}
