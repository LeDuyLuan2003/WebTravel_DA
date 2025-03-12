package com.SpringBootJdk22.SpringBootJdk22;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.SpringBootJdk22.SpringBootJdk22.controller.UserController;
import com.SpringBootJdk22.SpringBootJdk22.model.User;
import com.SpringBootJdk22.SpringBootJdk22.service.RoleService;
import com.SpringBootJdk22.SpringBootJdk22.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc để giả lập các yêu cầu HTTP

    @MockBean
    private UserService userService; // Mock UserService để tránh gọi DB

    @MockBean
    private RoleService roleService; // Mock RoleService để tránh lỗi

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;



    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login")) // Gửi yêu cầu GET đến /login
                .andExpect(status().isOk()) // Kiểm tra phản hồi có mã 200
                .andExpect(view().name("users/login")); // Kiểm tra view trả về là "users/login"
    }

    @Test
    public void testShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register")) // Gửi yêu cầu GET đến /register
                .andExpect(status().isOk()) // Kiểm tra phản hồi có mã 200
                .andExpect(view().name("users/register")) // Kiểm tra view trả về là "users/register"
                .andExpect(model().attributeExists("user")); // Kiểm tra model có chứa attribute "user"
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        // Giả lập userService.save() không làm gì cả
        doNothing().when(userService).save(any(User.class));
        doNothing().when(userService).setDefaultRole(anyString());

        mockMvc.perform(post("/register") // Gửi yêu cầu POST đến /register
                        .with(csrf()) // Thêm CSRF token
                        .param("username", "user") // Thêm tham số username
                        .param("password", "password") // Thêm tham số password
                        .param("email", "user@example.com") // Thêm tham số email
                        .param("phone", "0123456789")) // Thêm tham số phone
                .andExpect(status().is3xxRedirection()) // Kiểm tra phản hồi chuyển hướng
                .andExpect(redirectedUrl("/login")); // Kiểm tra chuyển hướng đến trang đăng nhập
    }

    @Test
    public void testRegisterFailure() throws Exception {
        // Kiểm thử với dữ liệu không hợp lệ
        mockMvc.perform(post("/register") // Gửi yêu cầu POST đến /register
                        .with(csrf()) // Thêm CSRF token
                        .param("username", "") // Thêm tham số username rỗng (lỗi validate)
                        .param("password", "") // Thêm tham số password rỗng (lỗi validate)
                        .param("email", "invalid-email") // Thêm tham số email không hợp lệ
                        .param("phone", "")) // Thêm tham số phone rỗng (lỗi validate)
                .andExpect(status().isOk()) // Kiểm tra phản hồi có mã 200
                .andExpect(view().name("users/register")) // Kiểm tra view trả về là "users/register"
                .andExpect(model().attributeExists("errors")); // Kiểm tra model có chứa attribute "errors"
    }




}
