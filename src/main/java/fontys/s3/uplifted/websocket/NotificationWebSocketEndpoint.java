    package fontys.s3.uplifted.websocket;

    import fontys.s3.uplifted.domain.dto.NotificationMessage;
    import jakarta.websocket.*;
    import jakarta.websocket.server.PathParam;
    import jakarta.websocket.server.ServerEndpoint;
    import lombok.extern.slf4j.Slf4j;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.stereotype.Component;

    import java.io.IOException;
    import java.util.*;
    import java.util.concurrent.ConcurrentHashMap;

    @Slf4j
    @Component
    @ServerEndpoint("/ws/{category}")
    public class NotificationWebSocketEndpoint {

        private static final Map<String, Set<Session>> categorySessions = new ConcurrentHashMap<>();
        private static final ObjectMapper mapper = new ObjectMapper();

        private Session session;
        private String category;

        @OnOpen
        public void onOpen(Session session, @PathParam("category") String category) {
            this.session = session;
            this.category = category;

            categorySessions.computeIfAbsent(category, k -> new HashSet<>()).add(session);
            log.info("🔗 WebSocket connected for category: {}", category);
        }

        @OnClose
        public void onClose() {
            if (category != null && session != null) {
                Set<Session> sessions = categorySessions.get(category);
                if (sessions != null) {
                    sessions.remove(session);
                    if (sessions.isEmpty()) {
                        categorySessions.remove(category);
                    }
                }
                log.info("❌ WebSocket disconnected from category: {}", category);
            }
        }

        @OnError
        public void onError(Session session, Throwable throwable) {
            log.error("⚠️ WebSocket error on category: {}", category, throwable);
        }

        @OnMessage
        public void onMessage(String msg) {
            // Optional: handle messages from client
            log.info("💬 Received from client: {}", msg);
        }

        public static void broadcast(NotificationMessage msg) {
            try {
                String json = mapper.writeValueAsString(msg);
                Set<Session> sessions = categorySessions.get(msg.getCategory());
                if (sessions != null) {
                    for (Session s : sessions) {
                        s.getAsyncRemote().sendText(json);
                    }
                    log.info("📢 Sent to {} listeners for category {}", sessions.size(), msg.getCategory());
                }
            } catch (IOException e) {
                log.error("Failed to serialize NotificationMessage", e);
            }
        }
    }
