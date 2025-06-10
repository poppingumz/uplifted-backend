package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.domain.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationMessage message) {
        System.out.println("🔔 Sending notification: " + message);
        messagingTemplate.convertAndSend("/topic/category/" + message.getCategory(), message);
    }
}
