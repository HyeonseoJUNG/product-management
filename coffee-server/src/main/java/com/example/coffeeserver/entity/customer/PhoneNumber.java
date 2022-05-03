package com.example.coffeeserver.entity.customer;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

public class PhoneNumber {
    private final String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        // data validation
        Assert.notNull(phoneNumber, "Phone Number ");

        // check length
        Assert.isTrue(phoneNumber.length() >= 11 && phoneNumber.length() <= 13, "Phone number length must be 13 characters.");

        // format 확인
        Assert.isTrue(checkAddress(phoneNumber), "Invalid phone number.");

        this.phoneNumber = phoneNumber;
    }

    /**
     * 들어온 값이 폰 번호 형식이 맞는지 확인
     * @param phoneNumber
     * @return 패턴과 일치하면 true
     */
    private static boolean checkAddress(String phoneNumber) {
        return Pattern.matches("^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*(\\d+))?)\\s*$", phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
