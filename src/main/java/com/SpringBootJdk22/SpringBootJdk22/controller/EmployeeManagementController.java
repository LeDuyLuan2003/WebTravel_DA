package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.DTO.UserDTO;
import com.SpringBootJdk22.SpringBootJdk22.service.UserService;
import com.SpringBootJdk22.SpringBootJdk22.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // Hiển thị danh sách tất cả người dùng có vai trò EMPLOYEE
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", userService.findUsersByRole("EMPLOYEE"));
        return "admin/employees/list";  // Trang hiển thị danh sách nhân viên
    }

    // Hiển thị form thêm người dùng mới với vai trò EMPLOYEE
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.findAllRoles());  // Hiển thị tất cả các vai trò
        return "admin/employees/add";  // Form thêm nhân viên
    }

    // Xử lý yêu cầu thêm người dùng mới với vai trò EMPLOYEE
    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin/employees/add";
        }
        userService.createEmployee(userDTO);
        return "redirect:/admin/employees";  // Chuyển hướng sau khi thêm thành công
    }

    // Hiển thị form chỉnh sửa thông tin người dùng với vai trò EMPLOYEE
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Optional<UserDTO> employee = userService.findEmployeeById(id);
        if (employee.isEmpty()) {
            return "redirect:/admin/employees";
        }
        model.addAttribute("user", employee.get());
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/employees/edit";  // Form chỉnh sửa nhân viên
    }

    // Xử lý yêu cầu chỉnh sửa thông tin người dùng với vai trò EMPLOYEE
    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, @Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin/employees/edit";
        }
        userService.updateEmployee(id, userDTO);
        return "redirect:/admin/employees";  // Chuyển hướng sau khi chỉnh sửa thành công
    }

    // Xử lý yêu cầu xóa người dùng với vai trò EMPLOYEE
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        userService.deleteEmployeeById(id);
        return "redirect:/admin/employees";  // Chuyển hướng sau khi xóa thành công
    }
}
