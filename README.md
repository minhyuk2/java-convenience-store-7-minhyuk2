# java-convenience-store-precourse - minhyuk2
# 편의점


## 개요
사용자에게 편의점의 남아 있는 물건을 출력하고, 프로모션의 적용 유무에 따라 사용자에게 다른 선택지를 제공하고,
프로모션이 없는 경우에는 멤버쉽할인을 진행해주는 프로그램


# 제출 전에 반드시 확인할 것

1. [x] 전체 테스트코드 작성
2. [] 단위 테스트코드 작성
3. [] 예외처리 진행
4. [] 함수 10줄 넘는지 확인
## 필요한 부분
1. [] 예외처리
2. [x] 전체 테스트 작성
3. [] 단위 테스트 작성
4. [x] 없는 물건에 대해서는 감소되게끔
5. [] 파일 입출력
6. [x] 프로모션 처리
7. [x] 멤버쉽 유무
8. [x] 추가 유무
9. [x] 마지막 영수증 처리
10. [x] 프로모션에서 상품을 적게 가져간 경우에 대한 처리
11. [x] 전체 물건들을 관리하게끔 사용하는 기능->객체로서 관리
12. [x] 할인 최대 금액만큼만 되게끔 설정
13. [x] null이 문자열로 출력되지 않게끔 설정
14. [x] 0개인 경우 0개가 아니라 재고 없음으로 출력
15. [x] md파일의 첫번째 문장을 어떻게 처리할건지에 대한 고민 -> 경로를 조건에서 주었기에 상대경로로 사
16. [x] 프로모션 제품이 프로모션을 받지 않은 경우 멤버쉽이 가능하게끔 처리

## 생각해볼 사안
    프로모션을 구입하고, 2+1인데 마지막 남은 2개를 산 경우
    -> 일반 재고 한 개만을 주면 되는 것인가? 아니면 1개는 정가 제공? 나머지는 정가제공
    -> 실행 예시를 보면 알 수 있듯이 남은 것에 대해서는 그냥 다 정가로 판매
    md파일의 첫번째 문장을 그냥 건너뛸 것인지,아니면 name으로 시작하면 건너뛸 것인지 고민
    그냥 첫번째 문장을 스킵하게끔 구현


## 기능 구현 정리
1. [x] 파일입출력으로 products.md에 적힌 현재 존재하는 물건들을 객체로 생성하는 기능
2. [x] 현재 갖고 있는 물건에 대한 정보를 출력하는 기능
3. [x] 구매할 상품을 입력받는 기능
4. [x] ,로 단어를 구분하고 []안에 있는지 판단하는 기능
5. [x] -로 물건과 수량 구분하는 기능
6. [x] 물건이 실제로 존재하는지 확인하는 기능
7. [x] 주문한 물건의 수량이 존재하는 수량보다 크지 않은지 확인하는 기능
8. [] 프로모션 상품부터 사용되게끔 만드는 기능
9. [x] 프로모션에 대한 정보를 가져오는 기능
10. [x] 현재날짜와 비교하여 프로모션의 적용을 판단하는 기능
11. [x] 구매할 상품이 프로모션 적용상품인지 판단하는 기능
12. [x] 구매할 상품의 재고가 모자르지 않는지 판단하는 기능
13. [x] 구매한 것들에 대한 계산을 하는 기능
14. [x] 영수증 발행하는 기능
15. [x] 추가 증정 물품 수령 선택 기능
16. [x] 추가 구매 상품 선택 기능
17. [x] 멤버쉽 선택 기능
18. [x] 물품 추가 선택 기능

## 예외처리
1. [x] "[" 로 시작했는데 "]" 로 닫히지 않은 경우
2. [x] 중간에 "[]"가 들어온 경우
3. [x] 개수를 -를 기준으로 자르기
4. [x] 공백이나 잘못된 값이 들어온 경우 다시 입력받기
5. [x] Y,N이 아닌 다른 값이 입력된 경우 예외처리
6. [x] y,n,Y,N 다 가능하게끔 처리





## 기능 요구 사항
구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.

사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.
총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
사용자가 잘못된 값을 입력할 경우 IllegalArgumentException를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
Exception이 아닌 IllegalArgumentException, IllegalStateException 등과 같은 명확한 유형을 처리한다.

### 재고 관리
각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.

### 프로모션 할인
오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.

### 멤버십 할인
멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
멤버십 할인의 최대 한도는 8,000원이다.

### 영수증 출력
영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
영수증 항목은 아래와 같다.
구매 상품 내역: 구매한 상품명, 수량, 가격
증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록

### 금액 정보
총구매액: 구매한 상품의 총 수량과 총 금액
행사할인: 프로모션에 의해 할인된 금액
멤버십할인: 멤버십에 의해 추가로 할인된 금액
내실돈: 최종 결제 금액
영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.


## 입출력 요구 사항
### 입력
구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
src/main/resources/products.md과 src/main/resources/promotions.md 파일을 이용한다.
두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.
구매할 상품과 수량을 입력 받는다. 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.

[콜라-10],[사이다-3]

프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
Y: 증정 받을 수 있는 상품을 추가한다.
N: 증정 받을 수 있는 상품을 추가하지 않는다.

프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
Y: 일부 수량에 대해 정가로 결제한다.
N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.

멤버십 할인 적용 여부를 입력 받는다.
Y: 멤버십 할인을 적용한다.
N: 멤버십 할인을 적용하지 않는다.

추가 구매 여부를 입력 받는다.
Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
N: 구매를 종료한다.

### 출력
환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 만약 재고가 0개라면 재고 없음을 출력한다.

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])

프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.
현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)

프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)

멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
멤버십 할인을 받으시겠습니까? (Y/N)

구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.\
===========W 편의점=============\
상품명		수량	금액\
콜라		3 	3,000\
에너지바 		5 	10,000\
===========증	정=============\
콜라		1\
==============================\
총구매액		8	13,000\
행사할인			-1,000\
멤버십할인			-3,000\
내실돈			 9,000

추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)


추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)

### 실행 결과 예시
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
    
    - 콜라 1,000원 10개 탄산2+1
      - 콜라 1,000원 10개
      - 사이다 1,000원 8개 탄산2+1
      - 사이다 1,000원 7개
      - 오렌지주스 1,800원 9개 MD추천상품
      - 오렌지주스 1,800원 재고 없음
      - 탄산수 1,200원 5개 탄산2+1
      - 탄산수 1,200원 재고 없음
      - 물 500원 10개
      - 비타민워터 1,500원 6개
      - 감자칩 1,500원 5개 반짝할인
      - 감자칩 1,500원 5개
      - 초코바 1,200원 5개 MD추천상품
      - 초코바 1,200원 5개
      - 에너지바 2,000원 5개
      - 정식도시락 6,400원 8개
      - 컵라면 1,700원 1개 MD추천상품
      - 컵라면 1,700원 10개
    
    구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    [콜라-3],[에너지바-5]
    
    멤버십 할인을 받으시겠습니까? (Y/N)
    Y
    
    ===========W 편의점=============\
    상품명		수량	금액\
    콜라		3 	3,000\
    에너지바 		5 	10,000\
    ===========증	정=============\
    콜라		1\
    ==============================\
    총구매액		8	13,000\
    행사할인			-1,000\
    멤버십할인			-3,000\
    내실돈			 9,000
    
    감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
    Y
    
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
    
    - 콜라 1,000원 7개 탄산2+1
      - 콜라 1,000원 10개
      - 사이다 1,000원 8개 탄산2+1
      - 사이다 1,000원 7개
      - 오렌지주스 1,800원 9개 MD추천상품
      - 오렌지주스 1,800원 재고 없음
      - 탄산수 1,200원 5개 탄산2+1
      - 탄산수 1,200원 재고 없음
      - 물 500원 10개
      - 비타민워터 1,500원 6개
      - 감자칩 1,500원 5개 반짝할인
      - 감자칩 1,500원 5개
      - 초코바 1,200원 5개 MD추천상품
      - 초코바 1,200원 5개
      - 에너지바 2,000원 재고 없음
      - 정식도시락 6,400원 8개
      - 컵라면 1,700원 1개 MD추천상품
      - 컵라면 1,700원 10개
    
    구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    [콜라-10]
    
    현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    Y
    
    멤버십 할인을 받으시겠습니까? (Y/N)
    N
    
    ===========W 편의점=============\
    상품명		수량	금액\
    콜라		10 	10,000\
    ===========증	정=============\
    콜라		2\
    ==============================\
    총구매액		10	10,000\
    행사할인			-2,000\
    멤버십할인			-0\
    내실돈			 8,000\
    
    감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
    Y
    
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
    
    - 콜라 1,000원 재고 없음 탄산2+1
      - 콜라 1,000원 7개
      - 사이다 1,000원 8개 탄산2+1
      - 사이다 1,000원 7개
      - 오렌지주스 1,800원 9개 MD추천상품
      - 오렌지주스 1,800원 재고 없음
      - 탄산수 1,200원 5개 탄산2+1
      - 탄산수 1,200원 재고 없음
      - 물 500원 10개
      - 비타민워터 1,500원 6개
      - 감자칩 1,500원 5개 반짝할인
      - 감자칩 1,500원 5개
      - 초코바 1,200원 5개 MD추천상품
      - 초코바 1,200원 5개
      - 에너지바 2,000원 재고 없음
      - 정식도시락 6,400원 8개
      - 컵라면 1,700원 1개 MD추천상품
      - 컵라면 1,700원 10개
    
    구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    [오렌지주스-1]
    
    현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    Y
    
    멤버십 할인을 받으시겠습니까? (Y/N)
    Y
    
    ===========W 편의점=============\
    상품명		수량	금액\
    오렌지주스		2 	3,600
    ===========증	정=============
    오렌지주스		1
    ==============================
    총구매액		2	3,600
    행사할인			-1,800
    멤버십할인			-0
    내실돈			 1,800
    
    감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
    N


## 프로그래밍 요구 사항 및 라이브러리
### 프로그래밍 요구 사항 1
    JDK 21 버전에서 실행 가능해야 한다.
    프로그램 실행의 시작점은 Application의 main()이다.
    build.gradle 파일은 변경할 수 없으며, 제공된 라이브러리 이외의 외부 라이브러리는 사용하지 않는다.
    프로그램 종료 시 System.exit()를 호출하지 않는다.
    프로그래밍 요구 사항에서 달리 명시하지 않는 한 파일, 패키지 등의 이름을 바꾸거나 이동하지 않는다.
    자바 코드 컨벤션을 지키면서 프로그래밍한다.
    기본적으로 Java Style Guide를 원칙으로 한다.

### 프로그래밍 요구 사항 2
    indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
    예를 들어 while문 안에 if문이 있으면 들여쓰기는 2이다.
    힌트: indent(인덴트, 들여쓰기) depth를 줄이는 좋은 방법은 함수(또는 메서드)를 분리하면 된다.
    3항 연산자를 쓰지 않는다.
    함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만들어라.
    JUnit 5와 AssertJ를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인한다.
    테스트 도구 사용법이 익숙하지 않다면 아래 문서를 참고하여 학습한 후 테스트를 구현한다.

### 프로그래밍 요구 사항 3
    else 예약어를 쓰지 않는다.
    else를 쓰지 말라고 하니 switch/case로 구현하는 경우가 있는데 switch/case도 허용하지 않는다.
    힌트: if 조건절에서 값을 return하는 방식으로 구현하면 else를 사용하지 않아도 된다.
    Java Enum을 적용하여 프로그램을 구현한다.
    구현한 기능에 대한 단위 테스트를 작성한다. 단, UI(System.out, System.in, Scanner) 로직은 제외한다.

### 프로그래밍 요구 사항 4
    함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
    함수(또는 메서드)가 한 가지 일만 잘 하도록 구현한다.
    입출력을 담당하는 클래스를 별도로 구현한다.
    아래 InputView, OutputView 클래스를 참고하여 입출력 클래스를 구현한다.
    클래스 이름, 메소드 반환 유형, 시그니처 등은 자유롭게 수정할 수 있다.
    public class InputView {
    public String readItem() {
    System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    String input = Console.readLine();    
    // ...
    }
    // ...
    }
    public class OutputView {
    public void printProducts() {
    System.out.println("- 콜라 1,000원 10개 탄산2+1");
    // ...
    }
    // ...
    }
### 라이브러리 
    camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.
    현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 now()를 활용한다.
    사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 readLine()을 활용한다.

## 디렉토리 구조
```angular2html
.
├── main
│   ├── java
│   │   └── store
│   │       ├── Application.java
│   │       ├── controller
│   │       │   └── BuyController.java
│   │       ├── domain
│   │       │   ├── Order.java
│   │       │   ├── Product.java
│   │       │   ├── Promotion.java
│   │       │   ├── Receipt.java
│   │       │   └── dto
│   │       │       ├── FilePathDTO.java
│   │       │       ├── InputDTO.java
│   │       │       ├── OrderDTO.java
│   │       │       ├── ProductDTO.java
│   │       │       └── PromotionDTO.java
│   │       ├── service
│   │       │   ├── BuyService.java
│   │       │   ├── OrderService.java
│   │       │   ├── ProductService.java
│   │       │   └── PromotionService.java
│   │       └── view
│   │           ├── ConvenienceInputView.java
│   │           └── ConvenienceOutputView.java
│   └── resources
│       ├── products.md
│       └── promotions.md
└── test
    └── java
        ├── resources
        │   ├── products.md
        │   └── promotions.md
        └── store
            ├── ApplicationTest.java
            ├── service
            │   ├── BuyServiceTest.java
            │   ├── OrderServiceTest.java
            │   ├── ProductServiceTest.java
            │   └── PromotionServiceTest.java
            └── view
                └── ConvenienceOutputViewTest.java

16 directories, 27 files
```