package com.company.performance.mapper;

import com.company.performance.common.enums.PositionType;
import com.company.performance.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("requires local mysql")
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void insertAndFindByUsername() {
        User user = new User();
        user.setUsername("test_user_1");
        user.setPassword("secret");
        user.setRealName("测试用户");
        user.setRoleId(1L);
        user.setDepartment("研发部");
        user.setPositionType(PositionType.RD);
        user.setStatus(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        User found = userMapper.findByUsername("test_user_1");
        assertNotNull(found);
        assertNotNull(found.getId());
        assertEquals("test_user_1", found.getUsername());
    }
}
