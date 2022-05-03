package com.example.coffeeserver.entity.coupon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Coupon {
    private final UUID couponId;
    private long discountRate;
    private LocalDate expirationDate;
    private UUID couponOwnerId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime issuedAt;

    public Coupon(UUID couponId, long discountRate, LocalDate expirationDate) {
        this.couponId = couponId;
        this.discountRate = discountRate;
        this.expirationDate = expirationDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getCouponId() {
        return couponId;
    }

    public long getDiscountRate() {
        return discountRate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setDiscountRate(long discountRate) {
        this.discountRate = discountRate;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCouponOwnerId(UUID ownerId) {
        this.couponOwnerId = ownerId;
        this.issuedAt = LocalDateTime.now();
    }
}
