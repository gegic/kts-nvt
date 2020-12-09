package rs.ac.uns.ftn.ktsnvt.kultura.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {


    @Value("dJe $i $ta cXini$")
    private static String secret;

    @Value("18000")
    private static Long expiration;


    public static String getToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        // JWT se prosledjuje kroz header Authorization u formatu:
        // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public static String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private static Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public static boolean validateToken(String token, User user) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        if (username == null || !username.equals(user.getUsername()))
            return false;
        else if (user.getLastPasswordChange() != null && created.before(Timestamp.valueOf(user.getLastPasswordChange())))
            return false;

        return true;
    }

    public static Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("email", user.getEmail());
        claims.put("created", new Date(System.currentTimeMillis()));
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

}
