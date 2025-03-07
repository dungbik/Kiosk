package com.example.constant;

public enum BenefitType {
    PATRIOT(1, "국가 유공자", 10),
    SOLDIER(2, "군인", 5),
    STUDENT(3, "학생", 3),
    NORMAL(4, "일반", 0)
    ;

    final int id;
    final String name;
    final int discountRate;

    BenefitType(int id, String name, int discountRate) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public static BenefitType parse(int id) {
        for (BenefitType benefitType : BenefitType.values()) {
            if (benefitType.getId() == id) {
                return benefitType;
            }
        }
        return NORMAL;
    }
}
