package project.restapi.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.restapi.constants.SecurityValues;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;

@Service
public class JwtUtil {
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        Claims body = Jwts.parser().setSigningKey(SecurityValues.SECRET_KEY).parseClaimsJws(token).getBody();
        return body;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String role = "";
        if (userDetails.getAuthorities().size() == 1) {
            role = "student";
        } else if (userDetails.getAuthorities().size() == 2) {
            role = "teacher";
        } else if (userDetails.getAuthorities().size() == 3) {
            role = "admin";
        }
        return createToken(claims, userDetails.getUsername(), role);
    }

    private String createToken(Map<String, Object> claimsUsername, String subjectUsername, String role) {
        return Jwts.builder()
                .setClaims(claimsUsername)
                .setSubject(format("%s,%s", subjectUsername, role))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SecurityValues.SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token).split(",")[0];
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}