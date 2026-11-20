package com.sudheer.ticket_booking.security;

import com.sudheer.ticket_booking.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {
    private final byte[] secret;
    private final long expirationSeconds;

    public JwtService(
        @Value("${security.jwt.secret:change-this-secret-in-production-to-a-long-random-value}") String secret,
        @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(User user) {
        long now = Instant.now().getEpochSecond();
        long exp = now + expirationSeconds;
        String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Url("{\"sub\":\"" + escape(user.getUsername()) + "\",\"role\":\"" + escape(user.getRole()) + "\",\"iat\":" + now + ",\"exp\":" + exp + "}");
        String unsigned = header + "." + payload;
        return unsigned + "." + base64Url(sign(unsigned));
    }

    public TokenData parseAndValidate(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String unsigned = parts[0] + "." + parts[1];
            byte[] expected = sign(unsigned);
            byte[] actual = Base64.getUrlDecoder().decode(parts[2]);
            if (!MessageDigest.isEqual(expected, actual)) return null;

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String subject = value(payload, "sub");
            String role = value(payload, "role");
            long exp = Long.parseLong(value(payload, "exp"));
            if (subject == null || role == null || Instant.now().getEpochSecond() >= exp) return null;
            return new TokenData(subject, role, exp);
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] sign(String input) {
    try {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret, "HmacSHA256"));
        return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
        throw new IllegalStateException("Failed to sign JWT", e);
    }
}

    private String base64Url(String value) { return base64Url(value.getBytes(StandardCharsets.UTF_8)); }
    private String base64Url(byte[] value) { return Base64.getUrlEncoder().withoutPadding().encodeToString(value); }
    private String escape(String value) { return value.replace("\\", "\\\\").replace("\"", "\\\""); }

    private String value(String json, String key) {
        String marker = "\"" + key + "\":";
        int start = json.indexOf(marker);
        if (start < 0) return null;
        start += marker.length();
        if (json.charAt(start) == '"') {
            int end = json.indexOf('"', start + 1);
            return end < 0 ? null : json.substring(start + 1, end);
        }
        int end = json.indexOf(',', start);
        if (end < 0) end = json.indexOf('}', start);
        return end < 0 ? null : json.substring(start, end);
    }

    public record TokenData(String username, String role, long expiresAt) {}
}
