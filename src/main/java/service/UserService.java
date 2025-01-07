package service;

import dto.UserDTO;
import mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDTO userDTO) {
        // 비밀번호 암호화
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // DB에 사용자 삽입
        userMapper.insertUser(userDTO);
    }
    
    public boolean validateUser(String email, String rawPassword) {
        String encodedPassword = userMapper.getPasswordByEmail(email);
        return encodedPassword != null && passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    public String getUserRole(String email) {
        return userMapper.getUserRole(email);
    }
    
    public boolean isIdDuplicated(String email) {
        // Use the UserMapper to check for ID duplication
        return userMapper.countByEmail(email) > 0;
    }
    
    public UserDTO getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }
    
    public boolean userExists(String email) {
        // 이메일로 사용자 존재 여부 확인
        return userMapper.findByEmail(email) != null;
    }

    public void deleteUser(String email) {
        // 사용자 삭제
        userMapper.deleteByEmail(email);
    }
}
