package com.example;

import com.example.constant.Category;
import com.example.service.Kiosk;
import com.example.domain.Menu;
import com.example.domain.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Kiosk kiosk = new Kiosk(getMenus());
        kiosk.start();
    }

    public static List<Menu> getMenus() {
        List<Menu> menus = new ArrayList<>();

        menus.add(
                new Menu(Category.BURGER, List.of(
                        new MenuItem(1, "ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"),
                        new MenuItem(2, "SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"),
                        new MenuItem(3, "Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"),
                        new MenuItem(4, "Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거")
                ))
        );

        menus.add(
                new Menu(Category.DRINK, List.of(
                        new MenuItem(1, "Strawberry Peach Tea", 5.0, "레몬에이드에 직접 우려낸 아이스티와 상큼한 딸기, 복숭아 맛을 더한 시즈널 레몬에이드"),
                        new MenuItem(2, "Lemonade", 4.5, "매장에서 직접 만드는 상큼한 레몬에이드"),
                        new MenuItem(3, "Fresh Brewed Iced Tea", 3.7, "직접 유기농 홍차를 우려낸 아이스 티"),
                        new MenuItem(4, "Fifty/Fifty", 4.0, "레몬에이드와 유기농 홍차를 우려낸 아이스 티 만나 탄생한 음료")
                ))
        );

        menus.add(
                new Menu(Category.DESSERT, List.of(
                        new MenuItem(1, "Shack Attack", 6.2, "진한 초콜릿 커스터드에 퍼지 소스와 세 가지 초콜릿 토핑이 블렌딩된 쉐이크쉑의 대표 콘크리트"),
                        new MenuItem(2, "Honey Butter Crunch", 6.2, "바닐라 커스터드에 허니 버터 소스와 슈가 콘이 달콤하게 블렌딩된 콘크리트"),
                        new MenuItem(3, "Better 2Gether", 6.2, "바닐라와 초콜릿 커스터드를 반씩 넣고 초콜릿 트러플 쿠키 도우와 쇼트브레드를 믹스한, 함께라서 더욱 특별한 콘크리트"),
                        new MenuItem(4, "Shack-ffogato", 6.2, "바닐라 커스터드에 커피 카라멜 소스, 초콜릿 토피, 초콜릿 청크, 코코아 파우더가 어우러진 쉐이크쉑만의 아포가토 콘크리트")

                ))
        );

        return menus;
    }
}
