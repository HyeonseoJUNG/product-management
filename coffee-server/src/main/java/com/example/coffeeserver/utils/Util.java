package com.example.coffeeserver.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Util {
    /**
     * byte를 가지고 UUID를 만들어주는 메소드
     * @param bytes
     * @return 변환된 UUID
     */
    public static UUID toUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
