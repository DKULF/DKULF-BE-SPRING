package test;

import security.JwtUtil;

public class tokengen {
public static void main(String[] args) {
	
	System.out.println();
	System.out.println(JwtUtil.generateAccessToken("admin@dankook.ac.kr", "ROLE_ADMIN"));
	System.out.println();
	System.out.println(JwtUtil.generateAccessToken("sample@dankook.ac.kr", "ROLE_USER"));
	System.out.println();
	System.out.println(JwtUtil.generateRefreshToken("sample@dankook.ac.kr"));
	
}
}
