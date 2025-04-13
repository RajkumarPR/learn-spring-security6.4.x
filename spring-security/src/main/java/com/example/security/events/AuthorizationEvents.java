package com.example.security.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEvents {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationEvents.class);

    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent) {
        log.error("Authorization failed for user: {} because authorization result: {}",
                deniedEvent.getAuthentication().get().getName(),
                deniedEvent.getAuthorizationResult().isGranted());


    }
}
