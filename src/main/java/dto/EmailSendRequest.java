package dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmailSendRequest {
    @ApiModelProperty(value = "사용자의 이메일 주소", example = "sample@dankook.ac.kr")
    private String email;
}