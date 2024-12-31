package mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.UserDTO;

public interface UserMapper {
	@Insert("INSERT INTO user (username, password, email, nickname) VALUES (#{username}, #{password}, #{email}, #{nickname});")
    void insertUser(UserDTO user);
	
	@Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countById(String username);
	
    @Select("SELECT username, password FROM user WHERE username = #{username}")
    UserDTO findByUsername(String username);
    
    @Select("SELECT password FROM user WHERE username = #{username}")
    String getPasswordByUsername(@Param("username") String username);

    @Select("SELECT role FROM user WHERE username = #{username}")
    String getUserRole(@Param("username") String username);
    
    @Select("SELECT username, nickname, email FROM user WHERE username = #{username}")
    UserDTO getUserByUsername(@Param("username") String username);
}