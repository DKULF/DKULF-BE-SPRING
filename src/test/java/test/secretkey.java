package test;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class secretkey {

	public static void main(String[] args) {
		System.out.println(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());;
	}
}
