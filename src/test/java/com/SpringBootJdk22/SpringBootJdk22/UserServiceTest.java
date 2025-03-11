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

import com.SpringBootJdk22.SpringBootJdk22.model.Role;
import com.SpringBootJdk22.SpringBootJdk22.model.User;
import com.SpringBootJdk22.SpringBootJdk22.repository.UserRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testLoadUserByUsername_Success() {
        // Chuẩn bị dữ liệu giả
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        // Gọi phương thức cần kiểm thử
        var userDetails = userService.loadUserByUsername("user");

        // Kiểm tra kết quả
        assertNotNull(userDetails);
        assertEquals("user", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Chuẩn bị dữ liệu giả
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        // Kiểm tra ngoại lệ
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("user");
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

        // Kiểm tra phương thức save được gọi
        verify(userRepository, times(1)).save(user);
    }
}
