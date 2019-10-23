package com.security.jwt;

import java.util.List;

public class JWTClaimDto {

	private String username;

	private List<String> permission;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getPermission() {
		return permission;
	}

	public void setPermission(List<String> permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "Username: " + username + ", permission:" + permission.toString();
	}

}
