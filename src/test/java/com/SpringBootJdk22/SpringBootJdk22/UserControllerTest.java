package com.SpringBootJdk22.SpringBootJdk22;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login")) // Gửi yêu cầu GET đến /login
                .andExpect(status().isOk()) // Kiểm tra phản hồi có mã 200
                .andExpect(view().name("users/login")); // Kiểm tra view trả về là "users/login"
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/login") // Gửi yêu cầu POST đến /login
                        .with(csrf()) //  THÊM CSRF TOKEN Ở ĐÂY
                        .param("username", "user") // Thêm tham số username
                        .param("password", "userpassword")) // Thêm tham số password
                .andExpect(status().is3xxRedirection()) // Kiểm tra phản hồi chuyển hướng
                .andExpect(redirectedUrl("/")); // Kiểm tra chuyển hướng đến trang chủ
    }

    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login") // Gửi yêu cầu POST đến /login
                        .with(csrf()) // THÊM CSRF TOKEN
                        .param("username", "wronguser") // Thêm tham số username sai
                        .param("password", "wrongpassword")) // Thêm tham số password sai
                .andExpect(status().is3xxRedirection()) // Kiểm tra phản hồi chuyển hướng
                .andExpect(redirectedUrl("/login?error")); // Kiểm tra chuyển hướng đến trang đăng nhập với tham số error
    }
}