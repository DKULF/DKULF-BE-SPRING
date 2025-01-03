package service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.univcert.api.UnivCert;

@Service
public class EmailVerificationService {

    private final String API_KEY = "79a49992-f87c-479b-9a55-592b9f290f9a";

    public Map<String, Object> requestVerificationCode(String email, String univName, boolean univCheck) {
    	  try {
    		  clearUser();
              // UnivCert 인증 요청
              Map<String, Object> response = UnivCert.certify(API_KEY, email, univName, univCheck);

              // 성공 여부 확인
              if ((boolean) response.get("success")) {
                  System.out.println("인증 코드가 발송되었습니다: " + response);
                  return response;
              } else {
                  String errorMessage = (String) response.get("message");
                  System.err.println("인증 코드 발송 실패: " + errorMessage);
                  return response;
              }
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      }
    
    public Map<String, Object> verifyCode(String email, String univName, int code) {
        try {
            // UnivCert 인증 코드 검증 요청
        	System.out.println(code + " // " +email);
            Map<String, Object> response = UnivCert.certifyCode(API_KEY, email, univName, code);
            return response;
        }  
            catch (Exception e) {
            e.printStackTrace();
            return null;
        }
   
    	}
	
	public void clearUser() {
		try {
		 UnivCert.clear(API_KEY);
		}
		catch (Exception e) {
	
		}
	}
}
