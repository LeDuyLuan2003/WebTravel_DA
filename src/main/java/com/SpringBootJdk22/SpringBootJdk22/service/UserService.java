package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.DTO.UserDTO;
import com.SpringBootJdk22.SpringBootJdk22.model.Role;
import com.SpringBootJdk22.SpringBootJdk22.model.User;
import com.SpringBootJdk22.SpringBootJdk22.repository.UserRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.RoleRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Lưu người dùng mới vào cơ sở dữ liệu sau khi mã hóa mật khẩu.
    public void save(@NotNull User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Gán vai trò mặc định cho người dùng dựa trên tên người dùng.
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    Role defaultRole = roleRepository.findByName("USER")
                            .orElseThrow(() -> new RuntimeException("Role USER not found"));
                    user.getRoles().add(defaultRole);
                    userRepository.save(user);
                },
                () -> { throw new UsernameNotFoundException("User not found"); }
        );
    }

    // Tải thông tin chi tiết người dùng để xác thực.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    // Tìm tất cả người dùng với một vai trò cụ thể
    public List<UserDTO> findUsersByRole(String roleName) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase(roleName)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tạo người dùng mới với vai trò EMPLOYEE
    public void createEmployee(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Role EMPLOYEE not found"));
        user.getRoles().add(employeeRole);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    // Tìm người dùng theo ID và trả về UserDTO
    public Optional<UserDTO> findEmployeeById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    // Cập nhật thông tin người dùng với vai trò EMPLOYEE
    public void updateEmployee(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone()); // Cập nhật thêm trường phone
        user.setProvider(userDTO.getProvider());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        userRepository.save(user);
    }

    // Xóa người dùng theo ID
    public void deleteEmployeeById(Long id) {
        userRepository.deleteById(id);
    }

    // Chuyển từ User Entity sang UserDTO
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone()) // Thêm phone vào DTO
                .email(user.getEmail())
                .provider(user.getProvider())
                .roles(user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()))
                .build();
    }

    // Chuyển từ UserDTO sang User Entity
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone()); // Thêm phone vào Entity
        user.setEmail(userDTO.getEmail());
        user.setProvider(userDTO.getProvider());
        return user;
    }
}
