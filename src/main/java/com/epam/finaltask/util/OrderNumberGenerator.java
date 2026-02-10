package com.epam.finaltask.util;

import com.epam.finaltask.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Component
public class OrderNumberGenerator {

    private final OrderRepository orderRepository;

    public String generateUniqueOrderNumber() {
        final int maxAttempts = 10;

        for (int i = 0; i < maxAttempts; i++) {
            String candidate = generateOrderNumberCandidate();
            if (!orderRepository.existsByOrderNumber(candidate)) {
                return candidate;
            }
        }
        return "ORD-" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }

    private String generateOrderNumberCandidate() {
        String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);

        String time = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HHmmss"));

        String rand = randomBase36Upper(6);

        return "ORD-" + date + "-" + time + "-" + rand;
    }

    private String randomBase36Upper(int len) {
        SecureRandom sr = new SecureRandom();
        final char[] alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet[sr.nextInt(alphabet.length)]);
        }
        return sb.toString();
    }
}
