package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import service.EmailVerificationService;
import swagger.Response;
import swagger.Response.EmailErrorResponseInvalidCode;
import swagger.Response.EmailErrorResponseNoRequest;
import swagger.Response.EmailVerifySuccessResponse;
	
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmailVerificationService verificationService;

    public AuthController(EmailVerificationService verificationService) {
        this.verificationService = verificationService;
    } 

    @PostMapping("/send")
    @ApiOperation(value = "이메일 전송 API", notes = "사용자 이메일로 인증 메일을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이메일 전송 성공", response = Response.EmailSuccessResponse.class),
            @ApiResponse(code = 400, message = "대학 도메인과 불일치", response = Response.EmailErrorResponse1.class),
            @ApiResponse(code = 401, message = "이미 인증된 이메일", response = Response.EmailErrorResponse2.class)
    })
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody String email) {
    	System.out.println(email);
    	 Map<String, Object> result = verificationService.requestVerificationCode(email,"단국대학교", true);
    	 if((boolean)result.get("success")) {
    		 return ResponseEntity.ok(result);
    	 }
        
    	 else {
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    	 }
    }
    @ApiOperation(value = "이메일 인증 API", notes = "사용자가 입력한 인증 코드를 검증합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "인증 성공", response = EmailVerifySuccessResponse.class),
        @ApiResponse(code = 400, message = "인증 코드 미일치", response = EmailErrorResponseInvalidCode.class),
        @ApiResponse(code = 401, message = "인증 요청 이력 없음", response = EmailErrorResponseNoRequest.class)
    })
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestParam String email, @RequestParam int code) {
    	 Map<String, Object> result = verificationService.verifyCode(email, "단국대학교", code);
    	 if(result == null) {
    		 result = new HashMap<String, Object>();
    		 result.put("message", "system error");
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    	 }
    	 if((boolean)result.get("success")) {
    		 result = new HashMap<String, Object>();
    		 result.put("success", true);
    		 return ResponseEntity.ok(result);
    	 }
    	 else {
    		 result.remove("code");
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    	 }
    }
}

