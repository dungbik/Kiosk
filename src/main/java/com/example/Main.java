package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("[ SHAKESHACK MENU ]");

            printMenuItem(1, "ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거");
            printMenuItem(2, "SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거");
            printMenuItem(3, "Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거");
            printMenuItem(4, "Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거");
            System.out.println("0. 종료");

            try {
                int command = sc.nextInt();
                if (command == 0) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("올바른 명령어를 입력해주세요.");
            }
        }

    }

    public static void printMenuItem(int command, String name, double price, String description) {
        System.out.printf("%d. %-16s | W %.1f | %s\n", command, name, price, description);
    }
}