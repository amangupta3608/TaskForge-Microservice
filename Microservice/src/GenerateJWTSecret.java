import java.security.SecureRandom;
import java.util.Base64;

public class GenerateJWTSecret {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 32 bytes = 256-bit key
        random.nextBytes(key);
        String jwtSecret = Base64.getEncoder().encodeToString(key);
        System.out.println("New JWT Secret: " + jwtSecret);
    }
}
