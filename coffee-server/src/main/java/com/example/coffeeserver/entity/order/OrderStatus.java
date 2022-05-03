package com.example.coffeeserver.entity.order;

public enum OrderStatus {
    PAYMENT_CONFIRMED,        // 결재 완료
    ACCEPTED,       // 주문 승인
    READY_FOR_DELIVERY,       // 배송 준비 완료
    SHIPPED,        // 배송 출발
    SETTLED,        // 수령 완료
    CANCELLED       // 상품 취소
}
