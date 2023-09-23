package com.example.hibernatetest2.security.event;

import java.util.Collection;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryImpl implements EventRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) {
        return jdbc.query(EventQuery.SELECT_EVENTS_BY_USER_ID_QUERY, Map.of("id", userId), new UserEventRowMapper());
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        jdbc.update(EventQuery.INSERT_EVENT_BY_USER_EMAIL_QUERY,
                    Map.of("email",
                           email,
                           "type",
                           eventType.toString(),
                           "device",
                           device,
                           "ipAddress",
                           ipAddress));
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {

    }
}