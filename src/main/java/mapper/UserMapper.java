package mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.UserDTO;

public interface UserMapper {
	@Insert("INSERT INTO user (email, password, nickname) VALUES (#{email}, #{password}, #{nickname});")
    void insertUser(UserDTO user);
	
	@Select("SELECT COUNT(*) FROM user WHERE Email = #{email}")
    int countByEmail(String email);
	
    @Select("SELECT email, password FROM user WHERE email = #{email}")
    UserDTO findByUsername(String email);
    
    @Select("SELECT password FROM user WHERE email = #{email}")
    String getPasswordByEmail(@Param("email") String email);

    @Select("SELECT role FROM user WHERE email = #{email}")
    String getUserRole(@Param("email") String email);
    
    @Select("SELECT email, nickname FROM user WHERE email = #{email}")
    UserDTO getUserByEmail(@Param("email") String email);
    
    @Select("SELECT * FROM user WHERE email = #{email}")
    UserDTO findByEmail(@Param("email") String email);

    @Delete("DELETE FROM user WHERE email = #{email}")
    void deleteByEmail(@Param("email") String email);
}