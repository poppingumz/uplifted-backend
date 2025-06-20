package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.domain.dto.NotificationMessage;
import fontys.s3.uplifted.websocket.NotificationWebSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationMessage message) {
        log.info("🔔 Sending native WebSocket notification: {}", message);
        NotificationWebSocketEndpoint.broadcast(message);
    }
}
