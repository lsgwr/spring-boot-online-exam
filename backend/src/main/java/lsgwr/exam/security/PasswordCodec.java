package lsgwr.exam.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class PasswordCodec {
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public String encode(String raw) {
        return bcrypt.encode(raw);
    }

    public boolean matches(String raw, String stored) {
        if (stored == null || raw == null) {
            return false;
        }
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return bcrypt.matches(raw, stored);
        }
        try {
            String decoded = new String(Base64.getDecoder().decode(stored), StandardCharsets.UTF_8);
            if (raw.equals(decoded)) {
                return true;
            }
        } catch (IllegalArgumentException ignored) {
            // legacy rows are base64; anything else is compared as plain text below
        }
        return raw.equals(stored);
    }
}
