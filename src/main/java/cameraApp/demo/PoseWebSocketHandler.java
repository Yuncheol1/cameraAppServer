package cameraApp.demo;

//import static cameraApp.demo.DemoApplication.db;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PoseWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Map<String, Set<WebSocketSession>> mobileSessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> unitySessions = new ConcurrentHashMap<>();

    // 세션ID → userId 매핑
    private final Map<String, String> sessionIdToUserId = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println(" WebSocket 연결됨: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload().trim();
        System.out.println(" 받은 메시지: " + payload);

        try {
            JsonNode json = mapper.readTree(payload);

            // 1. 등록 메시지 처리
            if (json.has("type") && "register".equals(json.get("type").asText())) {
                String userId = json.get("userId").asText();
                String role = json.get("role").asText();

                sessionIdToUserId.put(session.getId(), userId);

                if ("mobile".equals(role)) {
                    // Set으로 다중 모바일 세션 저장
                    mobileSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
                    System.out.println(" 모바일 등록됨: " + userId);
                } else if ("unity".equals(role)) {
                    unitySessions.put(userId, session);
                    System.out.println(" 유니티 등록됨: " + userId);
                }
                return;
            }

            if (json.has("landmarks")) {
                String userId = sessionIdToUserId.get(session.getId());
                if (userId == null) {
                    System.out.println("⚠ 등록되지 않은 세션: " + session.getId());
                    return;
                }

                WebSocketSession unitySession = unitySessions.get(userId);
                if (unitySession != null && unitySession.isOpen()) {
                    unitySession.sendMessage(message);
                    //db.InsertValues_Landmark(1, payload);
                    System.out.println(" 유니티에 데이터 전송 완료: " + userId);
                } else {
                    System.out.println("⚠ 유니티 세션이 없음: " + userId);
                }
            }

        } catch (Exception e) {
            System.out.println(" JSON 처리 실패: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println(" 연결 종료됨: " + session.getId());

        String userId = sessionIdToUserId.remove(session.getId());
        if (userId != null) {
            // 모바일 세션 제거
            Set<WebSocketSession> sessions = mobileSessions.get(userId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    mobileSessions.remove(userId);
                }
            }

            // 유니티 세션 제거
            WebSocketSession unitySession = unitySessions.get(userId);
            if (unitySession != null && unitySession.getId().equals(session.getId())) {
                unitySessions.remove(userId);
            }

            System.out.println(" 세션 정리 완료: " + userId);
        }
    }
}
