package com.example.service;

import com.example.constant.BenefitType;
import com.example.constant.Category;
import com.example.constant.Stage;
import com.example.domain.CartItem;
import com.example.domain.Menu;
import com.example.domain.MenuItem;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Kiosk {
    
    private final Scanner sc = new Scanner(System.in);

    private final List<Menu> menus;
    private final Cart cart = new Cart();

    private Stage curStage = Stage.SELECT_CATEGORY;
    private Category selectedCategory = null;
    private MenuItem selectedMenuItem = null;
    private BenefitType selectedBenefitType = null;

    public Kiosk(List<Menu> menus) {
        this.menus = menus;
    }

    /**
     * 키오스크 시스템의 메인 실행 루프를 시작합니다.
     * 이 메서드는 현재 단계와 사용자 상호작용에 따라 키오스크 운영의 다양한 단계를 지속적으로 전환합니다.
     * <p>
     * 이 메서드는 상태 변화를 관리하고 키오스크 워크플로우와 관련된 특정 작업을 호출합니다:
     * - SELECT_CATEGORY: 사용자가 카테고리를 선택할 수 있도록 지원합니다.
     * - SELECT_MENU_ITEM: 선택된 카테고리 내에서 메뉴 항목 선택을 처리합니다.
     * - CONFIRM_ADD_TO_CART: 선택된 항목을 장바구니에 추가할지 사용자 확인을 요청합니다.
     * - ADD_TO_CART: 선택된 항목을 장바구니에 추가합니다.
     * - CONFIRM_ORDER: 주문을 진행할지 사용자 확인을 요청합니다.
     * - CANCEL_ORDER: 진행 중인 주문을 취소하고 필요한 상태를 재설정합니다.
     * - REMOVE_CART_ITEM: 사용자가 장바구니에서 항목을 제거할 수 있도록 합니다.
     * - SELECT_BENEFIT: 사용자가 주문을 완료하기 전에 적용 가능한 혜택을 선택할 수 있도록 합니다.
     * - ORDER: 주문을 확정하고, 할인을 적용하며, 거래를 완료합니다.
     * <p>
     * 현재 단계가 Stage.EXIT로 전환되면 메서드는 종료됩니다.
     */
    public void start() {
        while (curStage != Stage.EXIT) {
            switch (curStage) {
                case SELECT_CATEGORY -> selectCategory();
                case SELECT_MENU_ITEM -> selectMenuItem();
                case CONFIRM_ADD_TO_CART -> confirmAddToCart();
                case ADD_TO_CART -> addToCart();
                case CONFIRM_ORDER -> confirmOrder();
                case CANCEL_ORDER -> cancelOrder();
                case REMOVE_CART_ITEM -> removeCartItem();
                case SELECT_BENEFIT -> selectBenefit();
                case ORDER -> order();
            }
        }
    }

    /**
     * 키오스크 시스템에서 카테고리 선택 과정을 처리합니다. 사용자는 사용 가능한 카테고리 항목을 선택하거나
     * 애플리케이션을 종료할 수 있습니다. 선택에 따라 키오스크의 현재 단계를 업데이트하고 선택된 항목을 처리합니다.
     * <p>
     * ORDER MENU 에 해당하는 카테고리 항목은 장바구니가 채워져 있을 때만 선택할 수 있습니다.
     */
    private void selectCategory() {
        printCategories();

        while (true) {
            int command = sc.nextInt();
            if (command == 0) {
                curStage = Stage.EXIT;
                break;
            } else if (command <= Category.values().length) {
                selectedCategory = Category.values()[command - 1];
                if (selectedCategory.isCartFilled() && !cart.isFilled()) {
                    System.out.println("현재 선택하실 수 없는 카테고리입니다.");
                    continue;
                }
                if (selectedCategory == Category.ORDER) {
                    curStage = Stage.CONFIRM_ORDER;
                } else if (selectedCategory == Category.CANCEL_ORDER) {
                    curStage = Stage.CANCEL_ORDER;
                } else if (selectedCategory == Category.REMOVE_CART_ITEM) {
                    curStage = Stage.REMOVE_CART_ITEM;
                } else {
                    curStage = Stage.SELECT_MENU_ITEM;
                }
                break;
            } else {
                System.out.println("존재하는 카테고리를 선택해주세요.");
            }
        }
    }

    /**
     * 키오스크 시스템에서 사용자에게 사용 가능한 카테고리를 표시합니다.
     * 이 메서드는 장바구니 상태에 따라 두 가지 주요 섹션을 출력합니다:
     * 1. MAIN MENU: 장바구니가 채워져 있지 않아도 접근 가능한 모든 카테고리를 나열합니다.
     * 2. ORDER MENU: 장바구니가 채워져 있을 경우, 선택 가능한 카테고리를 나열합니다.
     */
    private void printCategories() {
        System.out.println("[ MAIN MENU ]");
        Arrays.stream(Category.values())
                .filter(c -> !c.isCartFilled())
                .forEach(c -> System.out.printf("%d. %s\n", c.getId(), c.getTitle()));
        System.out.println("0. 종료");

        if (cart.isFilled()) {
            System.out.println();
            System.out.println("[ ORDER MENU ]");
            Arrays.stream(Category.values())
                    .filter(Category::isCartFilled)
                    .forEach(c -> System.out.printf("%d. %s\n", c.getId(), c.getTitle()));
        }
    }

    /**
     * 현재 카테고리의 메뉴에서 메뉴 항목을 선택하는 과정을 처리합니다.
     * <p>
     * 이 메서드는 사용자가 선택한 카테고리에 속한 메뉴 항목 목록을 표시하고,
     * 유효한 사용자 입력이 들어올 때까지 지속적으로 기다립니다:
     */
    private void selectMenuItem() {
        List<MenuItem> menuItems = menus.get(selectedCategory.ordinal()).menuItems();
        printItems(menuItems);

        while (true) {
            try {
                int command = sc.nextInt();
                if (command == 0) {
                    curStage = Stage.SELECT_CATEGORY;
                    break;
                } else if (command <= menuItems.size()) {
                    selectedMenuItem = menuItems.get(command - 1);
                    System.out.printf("선택한 메뉴 : %s\n", selectedMenuItem);
                    curStage = Stage.CONFIRM_ADD_TO_CART;
                    break;
                } else {
                    System.out.println("존재하는 메뉴를 선택해주세요.");
                }
            } catch (Exception e) {
                System.out.println("올바른 명령어를 입력해주세요.");
            } finally {
                System.out.println();
            }
        }
    }

    /**
     * 현재 선택된 카테고리의 메뉴 항목 목록을 표시합니다.
     * 각 항목은 사용자가 참고할 수 있도록 목록의 인덱스와 함께 표시됩니다.
     * 기본 종료 옵션이 포함됩니다.
     */
    private void printItems(List<MenuItem> menuItems) {
        System.out.printf("[ %s MENU ]\n", selectedCategory.name().toUpperCase());
        menuItems
                .forEach(menuItem ->
                        System.out.printf("%d. %s\n", menuItems.indexOf(menuItem) + 1, menuItem)
                );
        System.out.println("0. 종료");
    }

    /**
     * 선택된 메뉴 항목을 장바구니에 추가하는 확인 과정을 처리합니다.
     * <p>
     * 이 메서드는 선택된 메뉴 항목의 세부 정보를 출력하고, 사용자가 해당 항목을 장바구니에 추가할지
     * 또는 작업을 취소할지 선택하도록 요청합니다.
     */
    private void confirmAddToCart() {
        printConfirmAddToCart();

        while (true) {
            try {
                int command = sc.nextInt();
                if (command == 1) {
                    curStage = Stage.ADD_TO_CART;
                    break;
                } else if (command == 2) {
                    curStage = Stage.SELECT_CATEGORY;
                    break;
                } else {
                    System.out.println("존재하는 명령어를 선택해주세요.");
                }
            } catch (Exception e) {
                System.out.println("올바른 명령어를 입력해주세요.");
            } finally {
                System.out.println();
            }
        }
    }

    /**
     * 현재 선택된 메뉴 항목을 장바구니에 추가할지 사용자에게 확인 메시지를 표시합니다.
     */
    private void printConfirmAddToCart() {
        System.out.println(selectedMenuItem);
        System.out.println("위 메뉴를 장바구니에 추가하시겠습니까?");
        System.out.println("1. 확인\t\t2.취소");
    }

    /**
     * 현재 선택된 메뉴 항목을 쇼핑 카트에 추가하고 애플리케이션 상태를 업데이트합니다.
     */
    private void addToCart() {
        cart.addCartItem(selectedMenuItem);
        System.out.printf("%s 이 장바구니에 추가되었습니다.\n\n", selectedMenuItem.name());
        curStage = Stage.SELECT_CATEGORY;
    }

    /**
     * 주문을 진행하는 확인 과정을 처리합니다.
     * <p>
     * 이 메서드는 현재 주문 요약을 표시하고, 사용자가 주문을 확인하거나
     * 메뉴 선택으로 돌아갈 수 있도록 요청합니다.
     */
    private void confirmOrder() {
        printConfirmOrder();

        while (true) {
            try {
                int command = sc.nextInt();
                if (command == 1) {
                    curStage = Stage.SELECT_BENEFIT;
                    break;
                } else if (command == 2) {
                    curStage = Stage.SELECT_CATEGORY;
                    break;
                } else {
                    System.out.println("존재하는 명령어를 선택해주세요.");
                }
            } finally {
                System.out.println();
            }
        }
    }

    /**
     * 장바구니에 있는 아이템 목록과 주문 총 금액을 포함한 주문 확인 요약을 표시합니다.
     */
    private void printConfirmOrder() {
        System.out.println("[ ORDER ]");
        for (CartItem cartItem : cart.getCartItems()) {
            System.out.println(cartItem.getMenuItem());
        }
        System.out.println();
        System.out.println("[ Total ]");
        System.out.printf("W %f\n\n", cart.getTotalPrice());
        System.out.println("1. 주문\t\t2.메뉴판");
    }

    /**
     * 선택된 할인 혜택 유형을 기반으로 최종 가격을 계산하고, 주문 완료 메시지를 표시하며, 애플리케이션 상태를 재설정합니다.
     */
    private void order() {
        double discountPrice = applyDiscount(cart.getTotalPrice());
        System.out.printf("주문이 완료되었습니다. 금액은 W %f 입니다.\n\n", discountPrice);
        selectedBenefitType = null;
        cart.clear();
        curStage = Stage.SELECT_CATEGORY;
    }

    /**
     * 선택된 할인 혜택 유형을 기반으로 할인된 가격을 계산하여 반환한다.
     * @param price 장바구니에 다음 메뉴 가격의 총합
     * @return 할인이 적용된 최종 가격
     */
    private double applyDiscount(double price) {
        if (selectedBenefitType != BenefitType.NORMAL) {
            price = (100 - selectedBenefitType.getDiscountRate()) * price / 100.0D;
        }
        return price;
    }

    /**
     * 진행 중인 주문을 취소하고 시스템을 카테고리 선택 단계로 재설정합니다.
     */
    private void cancelOrder() {
        cart.clear();
        System.out.println("진행 중인 주문을 취소하였습니다.");
        System.out.println();
        curStage = Stage.SELECT_CATEGORY;
    }


    /**
     * 사용자 입력을 기반으로 쇼핑 카트에서 메뉴 항목을 제거합니다.
     * <p>
     * 이 메서드는 사용자가 쇼핑 카트에서 메뉴 항목을 검색할 키워드를 지정할 수 있도록 합니다.
     * 모든 일치하는 항목이 표시되며, 사용자는 제거할 항목을 선택하거나 작업을 취소할 수 있습니다.
     */
    private void removeCartItem() {
        List<MenuItem> foundMenuItem = findMenusItems();
        foundMenuItem
                .forEach(mi -> System.out.printf("%d. %s\n", foundMenuItem.indexOf(mi) + 1, mi));
        System.out.println("0. 취소");

        while (true) {
            try {
                int command = sc.nextInt();
                if (command == 0) {
                    curStage = Stage.SELECT_CATEGORY;
                    break;
                } else if (command <= foundMenuItem.size()) {
                    MenuItem selectedMenuItem = foundMenuItem.get(command - 1);
                    cart.removeCartItem(selectedMenuItem);
                    System.out.printf("%s 를 장바구니에서 제거하였습니다.", selectedMenuItem.name());
                    curStage = Stage.SELECT_CATEGORY;
                    break;
                } else {
                    System.out.println("존재하는 명령어를 선택해주세요.");
                }
            } catch (Exception e) {
                System.out.println("올바른 명령어를 입력해주세요.");
            } finally {
                System.out.println();
            }
        }
    }

    /**
     * 사용자가 제공한 키워드를 기반으로 쇼핑 카트에 있는 메뉴 항목의 목록을 찾아서 반환합니다.
     *
     * @return 제공된 키워드가 이름에 포함되어 있는 장바구니 내의 메뉴 항목 목록
     */
    private List<MenuItem> findMenusItems() {
        sc.nextLine();
        System.out.print("장바구니에서 제거할 메뉴의 이름 일부를 입력 : ");

        String keyword = sc.nextLine();
        List<MenuItem> foundMenuItem = cart.findMenuItemsByName(keyword);
        System.out.println("[Select Remove Cart Item]");
        return foundMenuItem;
    }

    /**
     * 혜택 유형을 선택하는 사용자 상호작용을 처리합니다.
     * 사용 가능한 혜택 옵션을 출력하고, 유효한 혜택 유형이 선택될 때까지
     * 사용자가 유효한 명령을 입력하도록 지속적으로 요청합니다.
     * <p>
     * 성공적인 선택이 이루어지면, 메서드는 애플리케이션 상태를 ORDER 단계로 전환합니다.
     * 입력 오류나 유효하지 않은 명령은 적절한 피드백을 제공하여 원활히 처리됩니다.
     */
    private void selectBenefit() {
        printSelectBenefit();

        while (true) {
            try {
                int command = sc.nextInt();
                if (command <= BenefitType.values().length) {
                    selectedBenefitType = BenefitType.parse(command);
                    curStage = Stage.ORDER;
                    break;
                } else {
                    System.out.println("존재하는 명령어를 선택해주세요.");
                }
            } catch (Exception e) {
                System.out.println("올바른 명령어를 입력해주세요.");
            } finally {
                System.out.println();
            }
        }
    }

    /**
     * 사용할 수 있는 할인 혜택 옵션을 콘솔에 출력합니다.
     */
    private static void printSelectBenefit() {
        System.out.println("할인 정보를 입력해주세요.");
        Arrays.stream(BenefitType.values())
                .forEach(c -> System.out.printf("%d. %-8s: %d%%\n", c.getId(), c.getName(), c.getDiscountRate()));
    }

}
