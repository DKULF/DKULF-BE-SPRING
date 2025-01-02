package test;

import java.util.Map;

import service.*;


public class verifyCodeTest {
	   public static void main(String[] args) {
	        // EmailVerificationService 인스턴스 생성
	        EmailVerificationService emailVerificationService = new EmailVerificationService();

	        // 테스트 데이터
	        String email = "kkjj1211@dankook.ac.kr";  // 테스트용 이메일
	        String univName = "단국대학교";       // 대학교 이름
	        int code = 5177;                   // 테스트용 인증 코드

	        // 메서드 호출
	        Map<String,Object> result = emailVerificationService.verifyCode(email, univName, code);

	        // 결과 출력
	        System.out.println(result);
	    }
}
