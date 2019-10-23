package com.security.jwt;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenProvider {

	private String secretKey = Base64.getEncoder().encodeToString("secretKey".getBytes());

	private long validityInMilliseconds = 3000; // 1s

	public static void main(String[] args) throws Exception {
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
		String token = jwtTokenProvider.createToken("test", "Perm1", "Perm2");
		System.out.println(token);
		// jwtTokenProvider.validateToken(token); to validate token

		// validate and get claim information
		System.out.println(jwtTokenProvider.getUserDetail(token));

	}

	public String createToken(String... permissions) {
		String randomUser = UUID.randomUUID().toString();
		Claims claims = Jwts.claims().setSubject(randomUser);
		JWTClaimDto jwtClaimDto = getJwtClaimDto(randomUser, permissions);
		claims.put("userDetail", jwtClaimDto);

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	private JWTClaimDto getJwtClaimDto(String username, String... permissions) {
		JWTClaimDto jwtClaimDto = new JWTClaimDto();
		jwtClaimDto.setPermission(Arrays.asList(permissions));
		jwtClaimDto.setUsername(username);
		return jwtClaimDto;
	}

	public JWTClaimDto getUserDetail(String token) throws IllegalArgumentException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return mapper.convertValue(claims.get("userDetail"), JWTClaimDto.class);
	}

	public void validateToken(String token) throws Exception {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		} catch (JwtException | IllegalArgumentException e) {
			throw new Exception("Expired or invalid JWT token");
		}
	}

}
