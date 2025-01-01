package swagger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Refresh Token 요청 모델")
public class RefreshTokenRequest {

    @ApiModelProperty(value = "JWT Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1wbGUiLCJpYXQiOjE3MzU3Mjc3MzEsImV4cCI6MTczNzExMDEzMX0.awfyhxRQajfGcKWRDw61jiQEb1Qm9RJBwSvrQ3ZoARk")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
