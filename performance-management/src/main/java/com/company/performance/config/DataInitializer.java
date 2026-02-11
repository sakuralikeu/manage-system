package com.company.performance.config;

import com.company.performance.common.enums.PositionType;
import com.company.performance.entity.Role;
import com.company.performance.entity.User;
import com.company.performance.mapper.RoleMapper;
import com.company.performance.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
        if (userMapper.findByUsername("admin") != null) {
            return;
        }
        Role role = roleMapper.findByRoleName("DIRECTOR");
        if (role == null) {
            return;
        }
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setRealName("系统管理员");
        user.setRoleId(role.getId());
        user.setDepartment("管理层");
        user.setPositionType(PositionType.PRESALE);
        user.setStatus(true);
        userMapper.insert(user);
    }
}
