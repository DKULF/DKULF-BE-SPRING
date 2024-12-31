package config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String API_NAME = "DKULF auth API docs";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "user/info 테스트 시에는 로그인에서 토큰을 발급받고 Authorization을 클릭하여 헤더를 'Bearer AccessToken' 형식으로 입력해주세요.\n 처음 실행 시 등록되어 있는 계정은 admin,admin / sample,sample 입니다. ";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
							
                .apiInfo(apiInfo())
                .select()
								
                .apis(RequestHandlerSelectors.basePackage("controller")) 
                .build()
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(apiKey())) // SecurityScheme 등록
                .securityContexts(Collections.singletonList(securityContext())); // 기본 응답 메시지 비활성화;
    }

		
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }
    
    // Bearer Token 설정
    private SecurityScheme apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    // SecurityContext 설정
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

    
}