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
    
    public boolean validateUser(String username, String rawPassword) {
        String encodedPassword = userMapper.getPasswordByUsername(username);
        return encodedPassword != null && passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    public String getUserRole(String username) {
        return userMapper.getUserRole(username);
    }
    
    public boolean isIdDuplicated(String id) {
        // Use the UserMapper to check for ID duplication
        return userMapper.countById(id) > 0;
    }
    
    public UserDTO getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
