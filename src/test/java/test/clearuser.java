package test;


import java.util.Map;

import service.EmailVerificationService;

public class clearuser {

    public static void main(String[] args) {
        // EmailVerificationService 인스턴스 생성
        EmailVerificationService emailVerificationService = new EmailVerificationService();

        emailVerificationService.clearUser();
    }
}
