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

    private void printItems(List<MenuItem> menuItems) {
        System.out.printf("[ %s MENU ]\n", selectedCategory.name().toUpperCase());
        menuItems
                .forEach(menuItem ->
                        System.out.printf("%d. %s\n", menuItems.indexOf(menuItem) + 1, menuItem)
                );
        System.out.println("0. 종료");
    }

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

    private void printConfirmAddToCart() {
        System.out.println(selectedMenuItem);
        System.out.println("위 메뉴를 장바구니에 추가하시겠습니까?");
        System.out.println("1. 확인\t\t2.취소");
    }

    private void addToCart() {
        cart.addCartItem(selectedMenuItem);
        System.out.printf("%s 이 장바구니에 추가되었습니다.\n\n", selectedMenuItem.name());
        curStage = Stage.SELECT_CATEGORY;
    }

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

    private void order() {
        double price = cart.getTotalPrice();
        if (selectedBenefitType != BenefitType.NORMAL) {
            price = (100 - selectedBenefitType.getDiscountRate()) * price / 100.0D;
        }
        System.out.printf("주문이 완료되었습니다. 금액은 W %f 입니다.\n\n", price);
        selectedBenefitType = null;
        cart.clear();
        curStage = Stage.SELECT_CATEGORY;
    }

    private void cancelOrder() {
        cart.clear();
        System.out.println("진행 중인 주문을 취소하였습니다.");
        System.out.println();
        curStage = Stage.SELECT_CATEGORY;
    }

    private void removeCartItem() {
        sc.nextLine();
        System.out.print("장바구니에서 제거할 메뉴의 이름 일부를 입력 : ");

        String keyword = sc.nextLine();
        List<MenuItem> foundMenuItem = cart.findMenuItemsByName(keyword);
        System.out.println("[Select Remove Cart Item]");
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

    private static void printSelectBenefit() {
        System.out.println("할인 정보를 입력해주세요.");
        Arrays.stream(BenefitType.values())
                .forEach(c -> System.out.printf("%d. %-8s: %d%%\n", c.getId(), c.getName(), c.getDiscountRate()));
    }

}
