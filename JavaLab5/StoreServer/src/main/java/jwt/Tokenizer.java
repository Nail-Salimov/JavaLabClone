package jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;

public class Tokenizer {
    private final static String secretWord = "6hdl3Mkkj75E0zGD";

    public static String getToken(int sub, String role) {
        try {
            String jwt = Jwts.builder().setSubject("afmch0832yc")
                    .claim("id", sub)
                    .claim("role", role)
                    .signWith(SignatureAlgorithm.HS256, secretWord.getBytes("UTF-8"))
                    .compact();
            return jwt;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean checkClient(String token, int sub, String role) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretWord.getBytes("UTF-8"))
                    .parseClaimsJws(token);
            int idToken = (Integer) claims.getBody().get("id");
            String roleToken = (String) claims.getBody().get("role");

            if ((idToken == sub) && (role.equals(roleToken))) {
                return true;
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
