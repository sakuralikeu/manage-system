package com.company.performance.mapper;

import com.company.performance.entity.Role;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("requires local mysql and seeded roles")
@SpringBootTest
class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void queryAllRolesAndByName() {
        List<Role> all = roleMapper.selectList(null);
        assertNotNull(all);
        assertTrue(all.size() >= 4);

        Role director = roleMapper.findByRoleName("DIRECTOR");
        assertNotNull(director);
        assertNotNull(director.getPermissions());
    }
}
