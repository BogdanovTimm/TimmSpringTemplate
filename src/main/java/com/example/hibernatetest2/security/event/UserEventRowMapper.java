package com.example.hibernatetest2.security.event;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserEventRowMapper implements RowMapper<UserEvent> {

    @Override
    public UserEvent mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return UserEvent.builder()
                        .id(resultSet.getLong("id"))
                        .type(resultSet.getString("type"))
                        .description(resultSet.getString("description"))
                        .device(resultSet.getString("device"))
                        .ipAddress(resultSet.getString("ip_address"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build();
    }
    
}