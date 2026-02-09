package com.company.performance.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/db")
public class DatabaseTimeController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseTimeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/time")
    public OffsetDateTime getDatabaseTime() {
        return jdbcTemplate.queryForObject("SELECT NOW()", (rs, rowNum) -> {
            Timestamp ts = rs.getTimestamp(1);
            return ts.toInstant().atOffset(ZoneOffset.ofHours(8));
        });
    }
}
