package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import service.UserService;
import swagger.Response;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(tags = "Default")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    @ApiOperation(value = "회원가입 API", notes = "사용자가 회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원가입 성공", response = Response.SignUpSuccessResponse.class),
            @ApiResponse(code = 400, message = "비밀번호가 불일치.", response = Response.PasswordMismatchResponse.class),
            @ApiResponse(code = 401, message = "비밀번호 길이 제한", response = Response.PasswordLengthErrorResponse.class),
            @ApiResponse(code = 402, message = "중복 Email", response = Response.DuplicateIdResponse.class)
    })
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDTO userDTO) {
    	   try {
    		   Map<String, Object> response = new HashMap<>();
               // Check if the ID is already taken
				
				  if (userService.isIdDuplicated(userDTO.getEmail())) {
				  response.put("statusCode", 400); response.put("success", false);
				  response.put("message", "이미 존재하는 Email입니다."); return
				  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); }
				 
               
               if (userDTO.getPassword().length() < 8) {
                   response.put("statusCode", 400);
                   response.put("success", false);
                   response.put("message", "비밀번호는 8자 이상이어야 합니다.");
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
               }
               
               // Check if the passwords match
               if (!userDTO.matchConfirmPassword()) {
                   response.put("statusCode", 400);
                   response.put("success", false);
                   response.put("message", "비밀번호가 일치하지 않습니다.");
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
               }
               

               
               // Register the user
               userService.registerUser(userDTO);
               
               response.put("statusCode", 200);
               response.put("success", true);
               response.put("message", "회원가입이 성공적으로 완료되었습니다.");
               return ResponseEntity.status(HttpStatus.CREATED).body(response);
           } catch (Exception e) {
        	   Map<String, Object> response = new HashMap<>();
               response.put("statusCode", 400);
               response.put("success", false);
               response.put("message", "회원가입에 실패했습니다.");
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
           }
       }
    
}
