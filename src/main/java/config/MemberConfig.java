package config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.DelegatingFilterProxy;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"mapper"})
@ComponentScan(basePackages= {"controller","service","security"})
@PropertySource("classpath:application.properties")
public class MemberConfig {
    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
    
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DataSource ds = new DataSource();
		//ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		//ds.setUrl("jdbc:mysql://localhost/spring5fs?characterEncoding=utf8");
		ds.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		ds.setUrl("jdbc:log4jdbc:mysql://localhost/DKULF_db?characterEncoding=utf8");
//		ds.setUrl("jdbc:log4jdbc:mysql://localhost/spring5fs?characterEncoding=utf8");


		ds.setUsername(username);
		ds.setPassword(password);
		ds.setInitialSize(2); 
		ds.setMaxActive(10);
		ds.setTestWhileIdle(true);
		ds.setMinEvictableIdleTimeMillis(60000 * 3);
		ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
		return ds;
	}
	

	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean se= new SqlSessionFactoryBean();
		se.setDataSource(dataSource());
		return (SqlSessionFactory)se.getObject();
	}
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
