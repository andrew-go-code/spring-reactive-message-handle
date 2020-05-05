package com.demo.center.listener;

import com.demo.center.service.MessageDistributor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppStartListener {
    private final MessageDistributor messageDistributor;

    @EventListener(ApplicationStartedEvent.class)
    public void onStart(){
        messageDistributor.start();
    }
}
