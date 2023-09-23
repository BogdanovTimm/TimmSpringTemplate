package com.example.hibernatetest2.security.event;

import java.util.Collection;

public interface EventService {

    /**
     * Takes all events from the database for given user
     */
    Collection<UserEvent> getEventsByUserId(Long userId);

    void addUserEvent(String email, EventType eventType, String device, String ipAddress);

    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);
}
