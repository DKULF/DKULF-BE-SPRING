package test;

import security.JwtUtil;

public class tokengen {
public static void main(String[] args) {
	
	System.out.println(JwtUtil.generateAccessToken("admin", "ROLE_ADMIN"));
	System.out.println();
	System.out.println(JwtUtil.generateAccessToken("sample", "ROLE_USER"));
	System.out.println();
	System.out.println(JwtUtil.generateRefreshToken("sample"));
}
}
