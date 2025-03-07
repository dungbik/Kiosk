package com.example;

import java.util.List;
import java.util.Scanner;

public class Kiosk {
    
    private final List<MenuItem> menuItems;
    
    public Kiosk(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("[ SHAKESHACK MENU ]");
            menuItems
                    .forEach(menuItem ->
                            System.out.printf("%d. %s\n", menuItems.indexOf(menuItem) + 1, menuItem)
                    );
            System.out.println("0. 종료");

            try {
                int command = sc.nextInt();
                if (command == 0) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                } else if (command <= menuItems.size()) {
                    MenuItem menuItem = menuItems.get(command - 1);
                    System.out.printf("선택한 메뉴 : %s\n", menuItem);
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

}
