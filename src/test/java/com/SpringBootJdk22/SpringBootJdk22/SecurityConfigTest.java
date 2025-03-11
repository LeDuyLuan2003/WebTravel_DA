package com.SpringBootJdk22.SpringBootJdk22;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAccessPublicEndpoint() throws Exception {
        mockMvc.perform(get("/")) // Gửi yêu cầu GET đến trang chủ
                .andExpect(status().isOk()); // Kiểm tra phản hồi có mã 200
    }

    @Test
    @WithMockUser(username = "admin", authorities  = "ADMIN")
    public void testAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin")) // Gửi yêu cầu GET đến /admin
                .andExpect(status().isOk()); // Kiểm tra phản hồi có mã 200
    }

    @Test
    @WithMockUser(username = "user", authorities  = "USER")
    public void testAccessAdminEndpoint_Forbidden() throws Exception {
        mockMvc.perform(get("/admin")) // Gửi yêu cầu GET đến /admin
                .andExpect(status().isForbidden()); // Kiểm tra phản hồi có mã 403
    }
}
