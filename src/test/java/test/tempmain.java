package test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class tempmain {
public static void main(String[] args) {
	PasswordEncoder a = new BCryptPasswordEncoder();
	System.out.println(a.encode("sample"));
}
}
