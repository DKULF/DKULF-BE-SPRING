package test;


import java.util.Map;

import service.EmailVerificationService;

public class SimpleTest {

    public static void main(String[] args) {
        // EmailVerificationService 인스턴스 생성
        EmailVerificationService emailVerificationService = new EmailVerificationService();

        // 테스트 데이터
        String email = "kkjj1211@naver.com";
        String univName = "단국대학교";
        boolean univCheck = false; // 재학 여부 확인 여부

        // 메서드 호출
        Map<String,Object>result = emailVerificationService.requestVerificationCode(email, univName, univCheck);

        // 결과 출력
        if ((Boolean)(result.get("success"))) {
            System.out.println(result.get("message"));
        } else {
            System.out.println(result.get("message"));
        }
    }
}
