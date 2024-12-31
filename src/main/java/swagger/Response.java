package swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class Response {
	@Data
	@ApiModel(description = "전송 성공")
	public class EmailSuccessResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "200")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "true")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "대학과 일치하지 않는 메일 도메인입니다.")
	    private String message;
	}
	
	@Data
	@ApiModel(description = "전송 실패")
	public class EmailErrorResponse1 {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "대학과 일치하지 않는 메일 도메인입니다.")
	    private String message;
	}
	
	@Data
	@ApiModel(description = "전송 실패")
	public class EmailErrorResponse2 {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "이미 완료된 요청입니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "인증 성공 응답")
	public class EmailVerifySuccessResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "200")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "true")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "인증이 완료되었습니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "인증 코드 미일치")
	public class EmailErrorResponseInvalidCode {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "일치하지 않는 인증코드입니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "인증 요청 이력이 없는 이메일")
	public class EmailErrorResponseNoRequest {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "인증 요청 이력이 존재하지 않습니다.")
	    private String message;
	}
	
	@Data
	@ApiModel(description = "로그인 성공 응답")
	public class LoginSuccessResponse {
	    @ApiModelProperty(value = "성공 여부", example = "true")
	    private boolean success;

	    @ApiModelProperty(value = "토큰 정보")
	    private Token token;

	    @Data
	    @ApiModel(description = "토큰 정보")
	    public class Token {
	        @ApiModelProperty(value = "액세스 토큰", example = "string")
	        private String accessToken;

	        @ApiModelProperty(value = "리프레시 토큰", example = "string")
	        private String refreshToken;
	    }
	}

	@Data
	@ApiModel(description = "로그인 실패 - 잘못된 입력")
	public class LoginErrorResponse1 {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "아이디 또는 비밀번호가 올바르지 않습니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "로그인 실패 - 입력값 없음")
	public class LoginErrorResponse2 {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "아이디 또는 비밀번호가 입력되지 않았습니다.")
	    private String message;
	}
	
	@Data
	@ApiModel(description = "Access Token 발급 성공 응답")
	public class TokenSuccessResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "200")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "true")
	    private boolean success;

	    @ApiModelProperty(value = "Access Token", example = "string")
	    private String accessToken;
	}

	@Data
	@ApiModel(description = "Refresh Token 만료 에러 응답")
	public class RefreshTokenExpiredResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "401")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "Refresh token이 만료되었습니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "유효하지 않은 Refresh Token 에러 응답")
	public class InvalidRefreshTokenResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "401")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "유효하지 않은 Refresh token 입니다.")
	    private String message;
	}
	
	@Data
	@ApiModel(description = "회원가입 성공 응답")
	public class SignUpSuccessResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "200")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "true")
	    private boolean success;

	    @ApiModelProperty(value = "응답 메시지", example = "회원가입이 성공적으로 완료되었습니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "비밀번호 불일치 에러 응답")
	public class PasswordMismatchResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "비밀번호가 일치하지 않습니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "비밀번호 길이 제한 에러 응답")
	public class PasswordLengthErrorResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "비밀번호는 8자 이상이어야 합니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "중복 ID 에러 응답")
	public class DuplicateIdResponse {
	    @ApiModelProperty(value = "HTTP 상태 코드", example = "400")
	    private int statusCode;

	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "에러 메시지", example = "이미 존재하는 ID입니다.")
	    private String message;
	}
	
	@Data
	@ApiModel(description = "성공 응답")
	public class UserInfoSuccessResponse {
	    @ApiModelProperty(value = "성공 여부", example = "true")
	    private boolean success;

	    @ApiModelProperty(value = "HTTP 상태 코드", example = "200")
	    private int statusCode;

	    @ApiModelProperty(value = "닉네임", example = "String")
	    private String nickname;

	    @ApiModelProperty(value = "이메일", example = "String")
	    private String email;
	}

	@Data
	@ApiModel(description = "JWT 토큰 에러 응답")
	public class JwtErrorResponse {
	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "HTTP 상태 코드", example = "401")
	    private int statusCode;

	    @ApiModelProperty(value = "에러 메시지", example = "JWT 토큰이 없거나 유효하지 않습니다.")
	    private String message;
	}

	@Data
	@ApiModel(description = "사용자 정보 없음 응답")
	public class UserNotFoundResponse {
	    @ApiModelProperty(value = "성공 여부", example = "false")
	    private boolean success;

	    @ApiModelProperty(value = "HTTP 상태 코드", example = "404")
	    private int statusCode;

	    @ApiModelProperty(value = "에러 메시지", example = "사용자 정보가 존재하지 않습니다.")
	    private String message;
	}
}
