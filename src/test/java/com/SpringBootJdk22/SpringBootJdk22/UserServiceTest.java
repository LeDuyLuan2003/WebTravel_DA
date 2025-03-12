package com.SpringBootJdk22.SpringBootJdk22;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.SpringBootJdk22.SpringBootJdk22.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.SpringBootJdk22.SpringBootJdk22.model.User;
import com.SpringBootJdk22.SpringBootJdk22.repository.UserRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.RoleRepository;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito cho test
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Tạo mock cho UserRepository

    @Mock
    private RoleRepository roleRepository; // Tạo mock cho RoleRepository

    @InjectMocks
    private UserService userService; // Inject các mock vào UserService

    @Test
    public void testLoadUserByUsername_Success() {
        // Chuẩn bị dữ liệu giả
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        // Giả lập khi gọi userRepository.findByUsername("user") sẽ trả về user

        // Gọi phương thức cần kiểm thử
        var userDetails = userService.loadUserByUsername("user");

        // Kiểm tra kết quả
        assertNotNull(userDetails); // Đảm bảo userDetails không null
        assertEquals("user", userDetails.getUsername()); // Kiểm tra username đúng
        assertEquals("password", userDetails.getPassword()); // Kiểm tra password đúng
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Chuẩn bị dữ liệu giả
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        // Giả lập khi gọi userRepository.findByUsername("user") sẽ trả về Optional rỗng

        // Kiểm tra ngoại lệ
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("user"); // Gọi phương thức và kiểm tra xem có ném UsernameNotFoundException không
        });
    }

    @Test
    public void testSaveUser() {
        // Chuẩn bị dữ liệu giả
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");

        // Gọi phương thức cần kiểm thử
        userService.save(user);

        // Kiểm tra phương thức save được gọi đúng 1 lần
        verify(userRepository, times(1)).save(user);
    }
}
