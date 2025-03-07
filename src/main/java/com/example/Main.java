package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"));
        menuItems.add(new MenuItem("SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"));
        menuItems.add(new MenuItem("Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"));
        menuItems.add(new MenuItem("Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거"));

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
