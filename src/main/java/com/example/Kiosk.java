package com.example;

import java.util.List;
import java.util.Scanner;

public class Kiosk {
    
    private final Scanner sc = new Scanner(System.in);

    private final List<Menu> menus;
    private Stage curStage = Stage.SELECT_CATEGORY;
    private Category selectedCategory = null;

    public Kiosk(List<Menu> menus) {
        this.menus = menus;
    }

    public void start() {
        while (curStage != Stage.SELECT_EXIT) {
            switch (curStage) {
                case SELECT_CATEGORY -> selectCategory();
                case SELECT_ITEM -> selectItem();
            }
        }
    }

    private void selectCategory() {
        printCategories();

        while (true) {
            int command = sc.nextInt();
            if (command == 0) {
                curStage = Stage.SELECT_EXIT;
                break;
            } else if (command <= Category.values().length) {
                selectedCategory = Category.values()[command - 1];
                curStage = Stage.SELECT_ITEM;
                break;
            } else {
                System.out.println("존재하는 카테고리를 선택해주세요.");
            }
        }
    }

    private static void printCategories() {
        System.out.println("[ MAIN MENU ]");
        for (int i = 0; i < Category.values().length; i++) {
            System.out.printf("%d. %s\n", i + 1, Category.values()[i].getTitle());
        }
        System.out.println("0. 뒤로가기");
    }

    private void selectItem() {
        List<MenuItem> menuItems = menus.get(selectedCategory.ordinal()).menuItems();
        printItems(menuItems);

        while (true) {
            try {
                int command = sc.nextInt();
                if (command == 0) {
                    curStage = Stage.SELECT_CATEGORY;
                    break;
                } else if (command <= menuItems.size()) {
                    MenuItem menuItem = menuItems.get(command - 1);
                    System.out.printf("선택한 메뉴 : %s\n", menuItem);
                    curStage = Stage.SELECT_CATEGORY;
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

}
