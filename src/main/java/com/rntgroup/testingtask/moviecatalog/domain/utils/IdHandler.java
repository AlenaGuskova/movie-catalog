package com.rntgroup.testingtask.moviecatalog.domain.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.BaseNCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class IdHandler {

    private static final String ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ*~$=U";
    private final Random random;
    private final BaseNCodec encoder;

    @Value("${application.id-size}")
    private int idSize;

    public String generateId() {
        byte[] bytes = new byte[idSize];
        random.nextBytes(bytes);
        int checksum = calculateChecksum(bytes);
        char checksumCharacter = getChecksumCharacter(checksum);
        return encoder.encodeToString(bytes) + checksumCharacter;
    }

    public boolean isValid(String identifier) {
        if (identifier == null || identifier.length() <= idSize) {
            return false;
        }
        int endIndex = identifier.length() - 1;
        byte[] value = identifier.substring(0, endIndex)
                .getBytes(StandardCharsets.UTF_8);
        byte[] decoded = encoder.decode(value);
        char checksumChar = identifier.charAt(endIndex);
        try {
            int checksum = calculateChecksum(decoded);
            return getChecksumCharacter(checksum) == checksumChar;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private int calculateChecksum(byte[] buffer) {
        return buffer == null || buffer.length == 0
                ? 0
                : ByteBuffer.wrap(buffer).getInt() % 37;
    }

    private char getChecksumCharacter(int checksumValue) {
        return ALPHABET.charAt(Math.abs(checksumValue));
    }
}
