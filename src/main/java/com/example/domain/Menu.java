package com.example.domain;

import com.example.constant.Category;

import java.util.List;

public record Menu(
        Category burger,
        List<MenuItem> menuItems
) {
}
