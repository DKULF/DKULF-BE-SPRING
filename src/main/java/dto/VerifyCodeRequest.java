package dto;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String email;
    private int code;
}