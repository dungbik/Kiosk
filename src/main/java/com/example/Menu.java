package com.example;

import java.util.List;

public record Menu(
        Category burger, List<MenuItem> menuItems
) {
}
