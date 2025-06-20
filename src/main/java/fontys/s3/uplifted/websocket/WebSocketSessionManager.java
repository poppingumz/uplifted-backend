package fontys.s3.uplifted.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.s3.uplifted.domain.dto.NotificationMessage;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionManager extends TextWebSocketHandler {

    private final Map<WebSocketSession, List<String>> sessionSubscriptions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionSubscriptions.put(session, new ArrayList<>());
        log.info("🟢 WebSocket connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessionSubscriptions.remove(session);
        log.info("🔴 WebSocket disconnected: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Map<String, Object> json = objectMapper.readValue(message.getPayload(), Map.class);
        String type = (String) json.get("type");

        if ("SUBSCRIBE".equalsIgnoreCase(type)) {
            String category = (String) json.get("category");
            sessionSubscriptions.getOrDefault(session, new ArrayList<>()).add(category);
            log.info("📩 Subscribed {} to category: {}", session.getId(), category);
        }
    }

    public void broadcastToCategory(String category, NotificationMessage msg) {
        TextMessage textMessage;
        try {
            textMessage = new TextMessage(objectMapper.writeValueAsString(msg));
        } catch (IOException e) {
            log.error("Failed to serialize message", e);
            return;
        }

        for (Map.Entry<WebSocketSession, List<String>> entry : sessionSubscriptions.entrySet()) {
            WebSocketSession session = entry.getKey();
            List<String> categories = entry.getValue();

            if (categories.contains(category) && session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    log.warn("Failed to send message to session {}", session.getId(), e);
                }
            }
        }
    }

    @PreDestroy
    public void cleanup() {
        sessionSubscriptions.keySet().forEach(session -> {
            try {
                session.close();
            } catch (IOException e) {
                log.warn("Error closing session {}", session.getId(), e);
            }
        });
    }
}
