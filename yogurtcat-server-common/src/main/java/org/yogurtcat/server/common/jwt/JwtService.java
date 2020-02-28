package org.yogurtcat.server.common.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

/**
 * Jwt相关服务
 * 
 * @author heaven
 *
 */
@Service
public class JwtService {

	private Clock clock = DefaultClock.INSTANCE;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	/**
	 * jwt tocken生成
	 * 
	 * @param username
	 * @return
	 */
	public String generateToken(String username) {
		final Date createdDate = clock.now();
		Map<String, Object> claims = new HashMap<>(16);
		final Date expirationDate = new Date(createdDate.getTime() + expiration);

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * jwt tocken刷新
	 * 
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		final Date createdDate = clock.now();
		final Date expirationDate = new Date(createdDate.getTime() + expiration);

		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * 用户名获取
	 * 
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	/**
	 * tocken校验
	 * 
	 * @param token
	 * @param name
	 * @return
	 */
	public Boolean validateToken(String token, String name) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		final Date expiration = claims.getExpiration();
		return !expiration.before(clock.now());
	}
}
