package demo.dashboard.service;

import demo.dashboard.model.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ActuatorDataService {
    private final RestTemplate restTemplate;

    @Async
    public CompletableFuture<String> getData(MessageService messageService) {
        try {
            return CompletableFuture.completedFuture(String.format("%s : %s", messageService.getName(), restTemplate.getForObject(messageService.getUrl(), String.class)));
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(String.format("%s : 0", messageService.getName()));
        }
    }
}
