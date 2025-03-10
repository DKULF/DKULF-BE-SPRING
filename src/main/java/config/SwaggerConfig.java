package config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
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
    private static final String API_DESCRIPTION = 
    		"응답 코드 중복시 에러가 발생하여 같은 코드를 반환해도 다른 코드로 설정하였습니다. statusCode를 확인해주세요.\n\n" +
            "### JWT 토큰 테스트 안내\n" +
            "user/info API를 사용하려면 JWT 토큰이 필요합니다. 아래의 토큰들을 `Authorize` 버튼을 클릭하여 추가한 다음 API를 호출하세요.\n\n" +
            "**관리자 계정 엑세스 토큰**\n" +
            "`eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBkYW5rb29rLmFjLmtyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE3MzU4MzI3ODYsImV4cCI6MTczNzMwMTU4Nn0._MFmyKH48LVDmJKuwWq1_hhniobQ6VczBuXwsyxD--4`\n\n" +
            "**Sample 계정 엑세스 토큰**\n" +
            "`eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1wbGVAZGFua29vay5hYy5rciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3MzU4MzI3ODgsImV4cCI6MTczNzMwMTU4OH0.fXS-LO6JDvQl4UvaxXzDb01oU8SV3Z8u440jK1KR0rs`\n\n" +
            "**Sample 계정 리프레쉬 토큰**\n" +
            "`eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1wbGVAZGFua29vay5hYy5rciIsImlhdCI6MTczNTgzMjc4OCwiZXhwIjoxNzM3MzAxNTg4fQ.b7ss4sLwTIUQqK3WyjBhL5mPyRMMxz8PLZLMpNM6LfM`\n\n" +
            "### 참고:\n" +
            "처음 실행 시 등록되어 있는 계정은 다음과 같습니다.\n" +
            "- 관리자 계정: `admin@dankook.ac.kr / admin`\n" +
            "- 샘플 계정: `sample@dankook.ac.kr / sample`\n";

    		
    @Bean
    public Docket api() {
    	
        return new Docket(DocumentationType.SWAGGER_2)	
                .apiInfo(apiInfo())
                .select()			
                .apis(RequestHandlerSelectors.basePackage("controller")) 
                .build()
                .produces(Set.of("application/json"))
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(apiKey())) // SecurityScheme 등록
                .securityContexts(Collections.singletonList(securityContext())) // 기본 응답 메시지 비활성화;
                .tags(new springfox.documentation.service.Tag("Default", "All APIs"));
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