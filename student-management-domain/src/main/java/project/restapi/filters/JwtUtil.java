package project.restapi.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.restapi.constants.SecurityValues;
import project.restapi.domain.entities.Administrator;
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.repository.AdministratorRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.repository.TeacherRepository;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;

@Service
public class JwtUtil {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AdministratorRepository administratorRepository;

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
        return Jwts.parser()
                .setSigningKey(SecurityValues.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = new HashSet<>();

        Optional<Student> student = studentRepository.findByUsername(userDetails.getUsername());
        Optional<Teacher> teacher = teacherRepository.findByUsername(userDetails.getUsername());
        Optional<Administrator> administrator = administratorRepository.findByUsername(userDetails.getUsername());

        student.ifPresent(s -> s.getAuthorities()
                .forEach(role -> roles.add(role.getAuthority())));

        teacher.ifPresent(t -> t.getAuthorities()
                .forEach(role -> roles.add(role.getAuthority())));

        administrator.ifPresent(a -> a.getAuthorities()
                .forEach(role -> roles.add(role.getAuthority())));

        claims.put("roles", roles.toArray());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subjectUsername) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subjectUsername)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SecurityValues.SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}