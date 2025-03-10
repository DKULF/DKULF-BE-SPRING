package dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDTO {
    private String password;
    private String passwordConfirm;
    @ApiModelProperty(hidden = true)
    private String role;
    private String email;
    private String nickname;
    
    public boolean matchConfirmPassword() {
        if (password == null || passwordConfirm == null) {
            return false; // null 체크
        }
        return password.equals(passwordConfirm);
    }
}
